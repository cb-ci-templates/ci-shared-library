// my-shared-library/vars/testPipeline.groovy

def call(pipelineParams) {
    def config=null

    pipeline {
        agent {
            kubernetes {
                yaml libraryResource("podtemplates/${pipelineParams.k8_agent_yaml}")
            }
        }
        stages {
            stage('Initialize') {
                steps {
                    script {
                        script {
                            // Ensure file operations are executed within a node block
                            config = readYaml file: pipelineParams.branchPropertiesFile
                            echo "Initializing pipeline with config: ${config}"
                        }
                    }
                    // Use the configuration from the YAML file
                    echo "Initializing pipeline with config: ${config}"
                }
            }
            // Other stages of your pipeline
        }
    }
}