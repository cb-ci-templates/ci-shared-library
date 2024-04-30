// my-shared-library/vars/testPipeline.groovy

def call(pipelineParams) {
    def config = readYaml file: pipelineParams.branchPropertiesFile

    pipeline {
        agent {
            kubernetes {
                yaml libraryResource("podtemplates/${pipelineParams.k8_agent_yaml}")
            }
        }
        stages {
            stage('Initialize') {
                steps {
                    // Use the configuration from the YAML file
                    echo "Initializing pipeline with config: ${config}"
                }
            }
            // Other stages of your pipeline
        }
    }
}