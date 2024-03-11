def call(Map configMap) {
    pipeline {
        agent {
            kubernetes {
                yamlFile "resources/podtemplates/${configMap.k8_agent_yaml}"
            }
        }
        parameters {
            string(name: 'greeting', defaultValue: "${configMap.param_greeting}",
                    description: 'How should I greet the world?')
        }
        stages {
            stage('Say Hello') {
                steps {
                    echo '${params.greeting}'
                    echo "${configMap.app}"
                }
            }
        }
    }

}

