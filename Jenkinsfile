pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        IMAGE_NAME = 'jenkins-docker-project'
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
                bat '''
                    @echo off
                    docker build -t %IMAGE_NAME%:1.0 .
                    if errorlevel 1 exit /b 1

                    docker build -t %IMAGE_NAME%:build-%BUILD_NUMBER% .
                    if errorlevel 1 exit /b 1

                    echo Docker images created:
                    docker images %IMAGE_NAME%
                '''
            }
        }

        stage('Run Container Test') {
            steps {
                bat '''
                    @echo off
                    set "CONTAINER_NAME=jenkins-docker-ci-%BUILD_NUMBER%"
                    set "IMAGE_TAG=%IMAGE_NAME%:build-%BUILD_NUMBER%"

                    echo Running image: %IMAGE_TAG%

                    docker run --name "%CONTAINER_NAME%" "%IMAGE_TAG%" > container-output.txt 2>&1

                    if errorlevel 1 (
                        type container-output.txt
                        exit /b 1
                    )

                    echo ==== Container output ====
                    type container-output.txt

                    echo ==== Validate application output ====
                    findstr /L /C:"Hello Docker CI/CD" container-output.txt
                    if errorlevel 1 exit /b 1

                    echo ==== Validate container exit code ====
                    docker inspect "%CONTAINER_NAME%" --format "{{.State.ExitCode}}" > container-exit-code.txt
                    if errorlevel 1 exit /b 1

                    set /p EXIT_CODE=<container-exit-code.txt
                    echo Container exit code: %EXIT_CODE%

                    if not "%EXIT_CODE%"=="0" exit /b 1

                    echo Container validation passed.
                '''
            }
            post {
                always {
                    bat '''
                        @echo off
                        set "CONTAINER_NAME=jenkins-docker-ci-%BUILD_NUMBER%"
                        docker rm -f "%CONTAINER_NAME%" 2>NUL
                        exit /b 0
                    '''
                }
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
            echo 'SUCCESS: Maven tests, Docker build, container validation, and artifact archiving completed.'
            echo "Docker image tag: ${env.IMAGE_NAME}:build-${env.BUILD_NUMBER}"
        }
        failure {
            echo 'FAILED: Check the Console Output to identify the failing stage.'
        }
        always {
            archiveArtifacts artifacts: 'target/*.jar,container-output.txt,container-exit-code.txt',
                             allowEmptyArchive: true,
                             fingerprint: true
        }
    }
}