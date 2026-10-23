pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build and Test') {
            steps {
                bat 'mvn clean package'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: false
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t jenkins-docker-project:1.0 .'
            }
        }

        stage('Run Container Test') {
            steps {
                bat '''
                    @echo off
                    set CONTAINER_NAME=jenkins-docker-ci-%BUILD_NUMBER%

                    docker run --name %CONTAINER_NAME% jenkins-docker-project:1.0 > container-output.txt 2>&1
                    if errorlevel 1 (
                        type container-output.txt
                        exit /b 1
                    )

                    type container-output.txt
                    findstr /L /C:"Hello Docker CI/CD!" container-output.txt
                    if errorlevel 1 exit /b 1

                    docker inspect %CONTAINER_NAME% --format "{{.State.ExitCode}}" > container-exit-code.txt
                    set /p EXIT_CODE=<container-exit-code.txt
                    echo Container exit code: %EXIT_CODE%

                    if not "%EXIT_CODE%"=="0" exit /b 1
                '''
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar,container-output.txt,container-exit-code.txt',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Maven tests passed, Docker image built, and container validation passed.'
        }
        failure {
            echo 'FAILED: Check the stage logs to identify the problem.'
        }
    }
}