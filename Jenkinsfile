
pipeline {
    agent any

    tools {
        jdk 'Java-17'
        maven 'Maven-3.9'
    }

    environment {
        SONAR_PROJECT_KEY = 'project5-java-app'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Project5SonarQube') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar \
                            -Dsonar.projectKey=$SONAR_PROJECT_KEY
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()

                        if (qg.status != 'OK') {
                            error "SonarQube Quality Gate failed: ${qg.status}"
                        }

                        echo "SonarQube Quality Gate passed: ${qg.status}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Build, tests, SonarQube analysis, and Quality Gate passed.'
        }

        failure {
            echo 'FAILURE: Check the failed pipeline stage and console output.'
        }

        always {
            echo 'Project 5 pipeline finished.'
        }
    }
}
