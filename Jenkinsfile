pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw -B clean test'
                    } else {
                        bat 'mvnw.cmd -B clean test'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw -B package -DskipTests'
                    } else {
                        bat 'mvnw.cmd -B package -DskipTests'
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}