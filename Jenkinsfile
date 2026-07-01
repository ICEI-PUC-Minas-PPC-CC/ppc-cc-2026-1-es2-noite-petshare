pipeline {
    agent any

    tools {
        maven 'Maven_3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                dir('src') {
                    script {
                        if (isUnix()) {
                            sh 'mvn clean test'
                        } else {
                            bat 'mvn clean test'
                        }
                    }
                }
            }
        }

        stage('Package') {
            steps {
                dir('src') {
                    script {
                        if (isUnix()) {
                            sh 'mvn package -DskipTests'
                        } else {
                            bat 'mvn package -DskipTests'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'src/target/surefire-reports/*.xml'
        }
    }
}