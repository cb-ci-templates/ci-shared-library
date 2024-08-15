// my-shared-library/vars/testPipeline.groovy

def call(globalConfig) {
    def mergedConfig=null

    pipeline {
        agent {
            kubernetes {
                yaml libraryResource("podtemplates/${globalConfig.k8_agent_yaml}")
            }
        }
        stages {
            stage('Initialize') {
                steps {
                    script {
                        script {
                            // Ensure file operations are executed within a node block
                            mergedConfig = initFromYaml globalConfig.branchPropertiesFile
                            //config = readYaml file: pipelineParams.branchPropertiesFile
                            echo "Initializing pipeline with config: ${mergedConfig}"
                        }
                    }
                    // Use the configuration from the YAML file
                    echo "Initializing pipeline with config: ${mergedConfig}"
                    echo (mergedConfig.ciTemplate)
                }
            }
            stage('Build') {
                steps {
                    container ("maven") {
                       //sh "mvn clean deploy"
                        sh "mvn -version"
                    }                 
                }
            }
            // Other stages of your pipeline
        }
    }
}
