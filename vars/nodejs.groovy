def call() {
  node() {
    common.pipelineINIT()
    stage('Download dependencies') {
      sh '''
        ls -ltr
        npm install
      '''
    }
  }
}



