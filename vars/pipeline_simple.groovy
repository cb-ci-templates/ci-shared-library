def call(Map configDefaults) {
    def config=null
    pipeline {
        agent {
            kubernetes {
                yaml libraryResource("podtemplates/podTemplate-init.yaml")
            }
        }
        stages {
            stage('Init') {
                steps {
                    script {
                        config = init  configDefaults
                    }
                    sh 'ls -la'
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
                    stage("deploy") {
                        steps {
                            sh "echo deploy "
                        }
                    }
                    //                  }
//                }
                }
            }
        }
    }

}