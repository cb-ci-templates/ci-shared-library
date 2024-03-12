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
            stage("Init") {
                steps {
                    //Init from yamnl
                    // init "./ci-config.yaml"
                    //Init from marker properties with defaults   (here ansible tower f.e)
                    defineProps('ci-config.properties', [ansibletower_async: 'true'])
                }
            }
            stage('Say Hello') {
                when ()
                steps {
                    echo "Greetings: ${params.greeting}"
                    echo "${pipelineParams.app}"
                    sh "echo ansibletower_async ${env.ansibletower_async}"
                    sh "echo key1 ${env.key1}"
                }
            }
        }
    }
}

