def call() {
  node() {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
        choice(choices: ['apply', 'destroy'], description: "Choose Action", name: "APPLY"),
      ]),
    ])
    ansiColor('xterm') {

      stage('Code Checkout') {
        sh 'find . | sed -e "1d" | xargs rm -rf'
        git branch: 'main', url: 'https://github.com/phani-devops-practice/roboshop-terraform-mutable.git'
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


