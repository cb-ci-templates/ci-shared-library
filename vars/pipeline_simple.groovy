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
                        }
                    }
                    container("yq") {
                        writeYaml file: 'ci-config-defaults.yaml', data: libraryResource("json/ci-config-defaults.yaml")
                        sh """
                            yq -m -x ${configDefaults.config.branchPropertiesFile} ci-config-defaults.yaml > config-merged.yaml
                        """
                        script {
                            config = readYaml file: config-merged.yaml
                        }
                    }
                    container("envsubst") {
                        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
                        writeYaml file: 'agent.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
                        sh "ls -la && envsubst < agent.yaml > tmp-podagent.yaml"
                        script {
                            env.MAVEN_IMAGE = config.build.maven.image
                            agentYaml = readYaml file: "tmp-podagent.yaml"
                        }
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
