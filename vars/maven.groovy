def call() {
  node() {
    common.pipelineINIT()
    stage('Compile packages') {
      sh 'mvn clean package'
    }
  }
}