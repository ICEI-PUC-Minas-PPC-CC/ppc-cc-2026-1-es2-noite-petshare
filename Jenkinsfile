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
                    docker.image('maven:3.9.8-eclipse-temurin-17').inside('-v $HOME/.m2:/root/.m2') {
                        sh 'mvn -B clean test'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    docker.image('maven:3.9.8-eclipse-temurin-17').inside('-v $HOME/.m2:/root/.m2') {
                        sh 'mvn -B package -DskipTests'
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