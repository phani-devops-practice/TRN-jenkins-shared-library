def pipelineINIT() {
  stage('Initiate repos') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/phani-devops-practice/${COMPONENT}.git"
  }
}

def publishArtifacts() {
  env.ENV="dev"
  stage('Prepare Artifacts') {
    if(env.APP_TYPE == "nodejs") {
      sh """
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip node_modules server.js
      """
    }
    if(env.APP_TYPE == "maven") {
      sh """
        cp target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar 
      """
    }
    if(env.APP_TYPE == "nginx") {
      sh """
        cd static
        zip -r ../${ENV}-${COMPONENT}-${TAG_NAME}.zip *
      """
    }
    if(env.APP_TYPE == "python") {
      sh """
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
      """
    }
    if(env.APP_TYPE == "golang") {
      sh """
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip main.go
      """
    }
  }
  stage('Push Artifacts to nexus') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'pass', usernameVariable: 'user')]) {
      sh """
        curl -v -u ${user}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
      """
    }
  }
  stage("deploy-to-any-dev") {
    build job: 'deploy-to-any-dev', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "${ENV}"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
  }
  stage("Run smoke test for dev") {
    sh "echo run smoke test"
  }
  promoteRelease("dev", "qa")

  stage("deploy-to-any-qa") {
//    build job: 'deploy-to-any-dev', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "${ENV}"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
    sh "echo QA deploy"
  }
  runTests()
  stage("Run smoke test for qa") {
    sh "echo run smoke test"
  }
  promoteRelease("qa", "prod")

}

def runTests() {
  stage('Quality checks') {
    parallel([
      IntigrationTests: {
        echo "intigration tests"
      },
      End2endTests: {
        echo "end2end tests"
      },
      PenetrationTests: {
        echo "penetration tests"
      }
    ])
  }
}


def promoteRelease(SOURCE_ENV, ENV) {
  stage("Promoting the artifact from ${SOURCE_ENV} to ${ENV}") {
    withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'pass', usernameVariable: 'user')]) {
      sh """
        cp ${SOURCE_ENV}-${COMPONENT}-${TAG_NAME}.zip ${ENV}-${COMPONENT}-${TAG_NAME}.zip
        curl -v -u ${user}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
      """
    }
  }
}

def codeChecks() {
  stage('Quality checks and Unit tests') {
    parallel([
      Qualitychecks: {
//        withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'pass', usernameVariable: 'user')]) {
//          sh "sonar-scanner -Dsonar.projectKey=${COMPONENT} -Dsonar.host.url=http://172.31.14.175:9000 -Dsonar.login=${user} -Dsonar.password=${pass} ${EXTRA_OPTS}"
//          sh "sonar-quality-gate.sh ${user} ${pass} 172.31.14.175 ${COMPONENT}"
         echo "quality checks"
      },
      unitTests: {
        unitTest()
      }
    ])
  }
}

def unitTest() {
  stage('Prepare Artifacts') {
    if (env.APP_TYPE == "nodejs") {
      sh """
        # npm run test
        echo run test cases
      """
    }
    if (env.APP_TYPE == "maven") {
      sh """
        # mvn test
        echo run test cases
      """
    }
    if (env.APP_TYPE == "nginx") {
      sh """
        # npm run test
        echo run test cases
      """
    }
    if (env.APP_TYPE == "python") {
      sh """
        # python -m unittest
        echo run test cases
      """
    }
    if (env.APP_TYPE == "golang") {
      sh """
        # go test
        echo run test cases 
      """
    }
  }
}