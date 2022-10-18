def call() {
  node() {
    common.pipelineINIT()
    stage('Download dependencies') {
      sh """
        go mod init dispatch
        go get
        go build
      """
    }
    common.codeChecks()
    if(env.BRANCH_NAME == env.TAG_NAME)
    {
     common.publishArtifacts()
    }
  }
}