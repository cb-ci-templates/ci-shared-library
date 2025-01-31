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
                        config = init configDefaults
                        agentYaml = initPodTemplate config
                    }
                }
            }
            stage('CI') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        //use the yaml file ref from ci-user-config
                        yaml libraryResource("podtemplates/${config.ci.maven.podyaml}")
                        //use the calculated agent 
                        //yaml agentYaml
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
            stage('CD') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        //yaml kubernetesPodTemplate(config)
                        yaml agentYaml
                        defaultContainer 'maven'
                        showRawYaml true
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
