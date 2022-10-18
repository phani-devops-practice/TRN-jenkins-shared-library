def call() {
  env.EXTRA_OPTS="-Dsonar.java.binaries=./target"
  node() {
    common.pipelineINIT()
    stage('Compile packages') {
      sh 'mvn clean package'
    }
    common.codeChecks()
    if(env.BRANCH_NAME == env.TAG_NAME)
    {
      common.publishArtifacts()
    }
  }
}