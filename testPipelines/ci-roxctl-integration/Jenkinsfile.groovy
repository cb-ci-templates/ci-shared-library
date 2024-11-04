library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-shared-library.git'])

// Building the data object
def configYaml = """---
ENDPOINT: 'test.end.point:443'
SKIP_TLS_VERIFY: 'true'
IMAGE_REGISTRY: 'docker.io'
IMAGE_NAME: 'curlimages/curl:latest'
"""
Map configMap = readYaml text: "${configYaml}"
println configMap


pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                    - name: shell
                      image: curlimages/curl:latest
                      runAsUser: 1000
                      command:
                        - cat
                      tty: true
                      workingDir: "/home/jenkins/agent"
                      securityContext:
                        runAsUser: 1000
                '''
            defaultContainer 'shell'
            retries 2
        }
    }
    stages {
        stage('Main') {
            steps {
                container ("shell"){
                    //Scan Image using roxctl cli
                    roxCTLScanImage configMap

                }
            }
        }
    }
}
