def pipelineINIT() {
    node() {
      stage('Initiate repos') {
        sh 'rm -rf *'
        git branch: 'main', url: "https://github.com/phani-devops-practice/cart.git"
      }
    }
}