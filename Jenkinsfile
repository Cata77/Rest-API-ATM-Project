pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Run Gradle build
                sh './gradlew build'
            }
        }

        stage('Test') {
            steps {
                // Run Gradle tests
                sh './gradlew test'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/libs/*.jar'
        }

        success {
            echo 'Build and tests passed successfully!'
        }

        failure {
            echo 'Build or tests failed!'
        }
    }

}