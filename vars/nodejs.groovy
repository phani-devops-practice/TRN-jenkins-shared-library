def call() {
  node() {
    common.pipelineINIT()
    stage('Download dependencies') {
      sh '''
        ls -ltr
        npm install
      '''
    }
    if(env.BRANCH_NAME == env.TAG_NAME)
    {
      common.publishArtifacts()
    }
  }
}



