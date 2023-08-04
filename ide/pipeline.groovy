pipeline {
    agent any
    triggers { pollSCM('* * * * *') }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/g0t4/jgsu-spring-petclinic.git', branch: 'main'
            }            
        }
        stage('Build') {
            steps {
                sh './mvnw clean package'
                //sh 'false' // true
            }
        
            post {
                always {
                    emailext attachLog: true, body: '${currentBuild.result}', compressLog: true, subject: '${JOB_NAME},${BUILD_NUMBER}', to: 'saad89.linux@gmail.com'

                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}