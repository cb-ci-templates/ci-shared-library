def call(Map configDefaults) {
    def config = null
    Map agentYaml = null
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
            stage('CD-image-envsubt') {
                //           parallel {
                //                stage ("runci"){

                agent {
                    kubernetes {
                        yaml """
                            kind: Pod
metadata:
  name: maven
spec:
  containers:
    - name: maven
      image: "${env.MAVEN_IMAGE}"
      #runAsUser: 1000
      command:
        - cat
      tty: true
      workingDir: "/home/jenkins/agent"
      #securityContext:
        #runAsUser: 1000
#      volumeMounts:
#        - name: maven-cache
#          mountPath: /tmp/.m2
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      imagePullPolicy: Always
      command:
        - /busybox/cat
      tty: true
      volumeMounts:
        - name: jenkins-docker-cfg
          mountPath: /kaniko/.docker
  volumes:
    #- name: maven-cache
    #  persistentVolumeClaim:
    #    claimName: maven-local-repo-cache
    - name: jenkins-docker-cfg
      projected:
        sources:
          - secret:
              name: docker-credentials
              items:
                - key: .dockerconfigjson
                  path: config.json

                        """
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
        }
    }
}
