pipeline {
    agent any
    triggers { pollSCM('* * * * *') }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/iSaadiqbal/course-jenkins-getting-started.git', branch: 'main'
            }            
        }
        stage('Build') {
            steps {
                sh './mvnw clean package'
               sh 'true' // true
            }
        
            post {
                always {
                    emailext (
                       subject: "Job '${JOB_NAME}' (build ${BUILD_NUMBER}) ${currentBuild.result}",
                        body: "Please go to ${BUILD_URL} and verify the build", 
                        attachLog: true, 
                        compressLog: true, 
                        to: "saad89.linux@gmail.com", 
                    )
                        
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}