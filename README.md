\# Jenkins + Docker CI Pipeline



\## Project Overview



This project demonstrates a Continuous Integration (CI) pipeline using Jenkins, Maven, Docker, and GitHub. Whenever code changes are pushed to GitHub, Jenkins automatically checks out the code, builds the Java application, runs unit tests, builds a Docker image, validates the running container, and archives build artifacts.



\## Architecture



Developer → GitHub → Jenkins → Maven Build \& Tests → Docker Image Build → Container Validation → Artifact Archiving



\## Technologies Used



\- Java 17

\- Apache Maven

\- Jenkins Pipeline

\- Docker Desktop

\- Git and GitHub

\- JUnit 5

\- Windows 11



\## Project Structure



```text

jenkins-docker-project/

├── src/

│   ├── main/java/App.java

│   └── test/java/AppTest.java

├── Dockerfile

├── .dockerignore

├── Jenkinsfile

├── pom.xml

└── README.md

```



\## Pipeline Stages



1\. \*\*Checkout:\*\* Retrieves the latest source code from GitHub.

2\. \*\*Maven Build and Test:\*\* Compiles the Java application, runs JUnit tests, and packages the JAR.

3\. \*\*Build Docker Image:\*\* Builds the application image and creates a build-specific image tag.

4\. \*\*Run Container Test:\*\* Runs the container and validates the application output and exit code.

5\. \*\*Archive Artifacts:\*\* Archives the packaged JAR and container validation files in Jenkins.



\## Docker Image Tagging



The pipeline creates two tags:



\- `jenkins-docker-project:1.0` — stable project tag.

\- `jenkins-docker-project:build-<BUILD\_NUMBER>` — unique tag associated with a Jenkins build.



Build-specific tags improve traceability between Jenkins builds and Docker images.



\## Automatic Build Trigger



Jenkins uses Poll SCM with the following schedule:



```text

\* \* \* \* \*

```



Jenkins checks the GitHub repository every minute and triggers a build when it detects a qualifying change.



\## Verification



The following have been verified during project development:



\- GitHub checkout and automatic SCM triggering.

\- Maven build and JUnit test execution.

\- Docker image creation.

\- Container application output validation.

\- Container exit code `0`.

\- Jenkins artifact archiving.

\- Build-specific Docker image tagging.

\- Temporary test-container cleanup.



Example application output:



```text

Hello Docker CI/CD - Build 2!

```



\## Troubleshooting



\### Dockerfile not found



Ensure the file is named `Dockerfile` without a `.txt` extension and is located in the repository root.



\### No main manifest attribute



Configure the Maven JAR plugin with `Main-Class: App`, then rebuild the JAR and Docker image.



\### Jenkinsfile not found



Configure the Jenkins job as \*\*Pipeline script from SCM\*\*, select Git, enter the repository URL, set the branch to `\*/main`, and use `Jenkinsfile` as the script path.



\### Container validation failure



Check the expected application output in the Jenkinsfile and confirm that the container exits with code `0`.



\## Learning Outcomes



\- Created a Jenkins pipeline integrated with GitHub.

\- Automated Java builds and unit tests using Maven.

\- Containerized a Java application using Docker.

\- Validated application behavior inside a container.

\- Added build-specific Docker image tags.

\- Archived build artifacts and handled temporary container cleanup.



\## Scope



This project demonstrates CI and local container validation. It does not include deployment to AWS or a production environment.



