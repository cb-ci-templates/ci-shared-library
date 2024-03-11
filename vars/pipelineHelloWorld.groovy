def call(Map pipelineParams) {
    pipeline {
        agent {
            kubernetes {
                yaml libraryResource("podtemplates/${pipelineParams.k8_agent_yaml}")
            }
        }
        parameters {
            string(name: 'greeting', defaultValue: "${pipelineParams.param_greetings}",
                    description: 'How should I greet the world?')
        }
        stages {
            stage('Say Hello') {
                steps {
                    echo "Greetings: ${params.greeting}"
                    echo "${pipelineParams.app}"
                }
            }
        }
    }
}

