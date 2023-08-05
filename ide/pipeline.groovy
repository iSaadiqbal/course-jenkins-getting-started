pipeline {
    agent any
    triggers { pollSCM('* * * * *') }
    
    stages {   
        stage('Checkout') {
            steps {
                git url: 'https://github.com/iSaadiqbal/jgsu-spring-petclinic.git', branch: 'main' }            
        }
        stage('compile') {
            steps {sh './mvnw clean compile'}
        }
        stage('Build') {
            steps {sh './mvnw clean package'}
        }
        stage('test logs') {
            steps { junit '**/target/surefire-reports/TEST-*.xml'}
        }
        stage('download logs') { 
            steps {archiveArtifacts 'target/*.jar' }
        }
    }
        post {
                always {
                    emailext (
                       subject: "Job '${JOB_NAME}' (build ${BUILD_NUMBER}) ${currentBuild.result}",
                        body: "Please go to ${BUILD_URL} and verify the build", 
                        attachLog: true, 
                        to: "saad89.linux@gmail.com", 
                    )     
                }
            }
    }