def call(Map configDefaults) {
    def config=null
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
                        config = init  configDefaults
                        env.MAVEN_IMAGE=config.build.maven.image
                        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
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
            stage('CI-image-envsubt') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        yaml libraryResource("podtemplates/podTemplate-envsubt-images.yaml")
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