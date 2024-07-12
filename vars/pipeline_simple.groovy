def call(Map configDefaults) {
    def config = null
    def agentYaml = null
    pipeline {
        agent none
        stages {
            stage('Init') {
                agent {
                    kubernetes {
                        yaml libraryResource("podtemplates/podTemplate-init.yaml")
                    }
                }
                steps {
                    container("json-schema-validator") {
                        script {
                            config = init configDefaults
                            env.MAVEN_IMAGE = config.build.maven.image
                            //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
                        }
                    }

                    container("yq") {
                        writeYaml file: 'ci-config-defaults.yaml', data: libraryResource("json/ci-config-defaults.yaml")
                        sh """
                            cat ci-config-defaults.yaml
                            cat ${configDefaults.branchPropertiesFile} 
                            #cat ${configDefaults.branchPropertiesFile} > config-merged.yaml
                            #cat ci-config-defaults.yaml >> config-merged.yaml
                            yq eval ${configDefaults.branchPropertiesFile} ci-config-defaults.yaml > config-merged.yaml
                            sed -i '/---/d' config-merged.yaml
                            cat config-merged.yaml
                        """
                         script {
                             config = readYaml file: "config-merged.yaml"
                         }
                    }

                    container("envsubst") {
                        writeYaml file: 'agent.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
                        sh '''
                            ls -la 
                            cat agent.yaml
                            envsubst < agent.yaml > tmp-podagent.yaml
                            sed -i '1d' tmp-podagent.yaml #workartund
                            cat tmp-podagent.yaml
                        '''
                        script{
                            agentYaml = readYaml file: "tmp-podagent.yaml"
                        }
                        sh "echo $agentYaml"
                    }
                }
            }
            stage('CI') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        yaml libraryResource("podtemplates/${config.build.maven.podyaml}")
                    }
                }
                stages {
                    stage("build") {
                        steps {
                            sh "echo build "
                        }
                    }
                    stage("create image") {
                        steps {
                            sh "echo image "
                        }
                    }
                    stage("test") {
                        steps {
                            sh "echo image "
                        }
                    }
                    stage("qa scans") {
                        steps {
                            parallel(
                                    a: {
                                        container("maven") {
                                            echo "This is branch a"
                                        }
                                    },
                                    b: {
                                        container("maven") {
                                            echo "This is branch b"
                                        }
                                    }
                            )
                        }
                    }
                    //                  }
//                }
                }
            }
            stage('CD-image-envsubt') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        yaml agentYaml
                    }
                }
                stages {
                    stage("deploy") {
                        steps {
                            sh "echo deploy "
                        }
                    }
                    stage("test") {
                        steps {
                            sh "echo test "
                        }
                    }
                    //                  }
//                }
                }
            }
        }
    }
}
