node {
    checkout scm

    String buildTool = '.'
    String repo = '.'

    stage('Set Build Tool') {
        repo = sh (script: 'git diff-tree --no-commit-id --name-only HEAD', returnStdout: true).trim()
        if (repo == 'student-service') {
            buildTool = 'maven'
            return
        }
        if (repo == 'subject-service') {
            buildTool = 'gradle'
            return
        }
    }

    stage('Test') {
        dir(repo) {
            if (buildTool == 'maven') {
                withMaven(maven: 'maven-3') {
                    sh 'mvn clean verify'
                }
            }
            if (buildTool == 'gradle') {
                withGradle {
                    sh './gradlew test'
                }
            }
        }
    }
}