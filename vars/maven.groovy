def call() {
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