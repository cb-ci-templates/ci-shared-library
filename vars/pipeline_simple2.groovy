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
                    script {
                        config=init configDefaults
                        agentYaml=initPodTemplate config
                    }
                    sh "echo ${agentYaml}"
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
                            parallel(a: {
                                container("maven") {
                                    echo "This is branch a"
                                }
                            },
                                    b: {
                                        container("maven") {
                                            echo "This is branch b"
                                        }
                                    })
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
                        //yamlFile config.dynamicPodTemplateFile
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
