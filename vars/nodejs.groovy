def call() {
  node() {

    stage('Download dependencies') {
      sh '''ls -ltr
      npm install'''
    }
  }
}



