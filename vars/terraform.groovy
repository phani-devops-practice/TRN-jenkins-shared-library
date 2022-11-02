def call() {
  node() {
    ansiColor('xterm') {

      stage('Code Checkout') {
        sh 'rm -rf *'
      }
      stage('Terraform Init') {
        sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
      }
      stage('Terraform Plan') {
        sh 'terraform plan -var-file=env/${ENV}.tfvars'
      }
      stage('Terraform Apply') {
        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}


//
//def call() {
//  node() {
//    properties([
//      parameters([
//        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
//      ]),
//    ])
//
//    ansiColor('xterm') {
//
//      stage('Code Checkout') {
//        sh 'rm -rf *'
//        git branch: 'main', url: 'https://github.com/phani-devops-practice/roboshop-terraform-mutable.git'
//      }
//
//      stage('Terraform Init') {
//        sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
//      }
//      stage('Terraform Plan') {
//        sh 'terraform plan -var-file=env/${ENV}.tfvars'
//      }
//      stage('Terraform Apply') {
//        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
//      }
//    }
//  }
//}