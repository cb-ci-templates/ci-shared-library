
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
                        agentYaml = initPodTemplate config
                    }
                }
            }
            stage('CI') {
                agent {
                    kubernetes {
                        //use the yaml file ref from ci-user-config
                        yaml libraryResource("podtemplates/${config.build.maven.podyaml}")
                        //use the calculated agent
                        //yaml agentYaml
                    }
                }
                stages {
                    stage("build") {
                        steps {
                            container("maven") {
                                //https://plugins.jenkins.io/pipeline-maven/
                                withMaven(
                                        //Use `$WORKSPACE/.repository` for local repository folder to avoid shared repositories
                                        //consider to use CloudBees Workspace caching https://docs.cloudbees.com/docs/cloudbees-ci/latest/pipelines/cloudbees-cache-step
                                        mavenLocalRepo: '/tmp/.m2',
                                        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                                        // We recommend to define Maven settings.xml globally at the folder level using
                                        // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
                                        // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
                                        //mavenSettingsConfig: 'global-maven-settings'
                                ) {
                                  /*
                                    config.build.maven.steps.each { step ->
                                        sh step
                                    }
                                    */

                                    sh config.build.maven.steps[1]
                                    //https://docs.cloudbees.com/docs/cloudbees-ci/latest/pipelines/cloudbees-cache-step
                                    //writeCache name: 'maven-repo', includes: '.m2/repository/**', excludes: '**SNAPSHOT**'
                                }
                                junit '**/target/surefire-reports/TEST-*.xml'
                                archive '**/target/*.jar'
                            }
                        }
                    }
                    stage("Docker") {
                        when {
                            branch 'main'
                        }
                        options {
                            skipDefaultCheckout(true)
                        }
                        steps {
                            container(name: 'kaniko', shell: '/busybox/sh') {
                                withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
                                    sh '''#!/busybox/sh
                          /kaniko/executor  --dockerfile $(pwd)/Dockerfile --insecure --skip-tls-verify --cache=false  --context $(pwd) --destination caternberg/spring-boot-demo:${GIT_COMMIT_SHORT}
                      '''
                                }
                            }
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