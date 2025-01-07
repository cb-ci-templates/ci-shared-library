def call(Map configDefaults) {
    Map config = [:]
    def agentYaml = null
    pipeline {
        agent none
        options {
            buildDiscarder(logRotator(numToKeepStr: '5'))
            //https://www.jenkins.io/blog/2018/02/22/cheetah/}
            //https://www.jenkins.io/doc/book/pipeline/scaling-pipeline/
            durabilityHint('PERFORMANCE_OPTIMIZED')
        }
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
                        //agentYaml = initPodTemplate config
                    }
                }
            }
            stage('CI') {
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
                            build config
                        }
                    }
                    stage("Image") {
                        when {
                            branch 'main'
                        }
                        options {
                            skipDefaultCheckout(true)
                        }
                        steps {
                            kaniko config ${GIT_COMMIT_SHORT}
                          /*  container(name: 'kaniko', shell: '/busybox/sh') {
                                withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
                                    sh '''#!/busybox/sh
                          /kaniko/executor  --dockerfile $(pwd)/Dockerfile --insecure --skip-tls-verify --cache=false  --context $(pwd) --destination caternberg/spring-boot-demo:${GIT_COMMIT_SHORT}
                      '''
                                }
                            } */
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
                }
            }
            stage('CD') {
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
                }
            }
        }
    }
}