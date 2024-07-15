def call(Map config) {
    Map agentYaml=null
    container("yq") {
        // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        //agentRef=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        writeYaml file: podTemplateFilePath, data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            #cat agentTemplate.yaml |envsubst |yq > gen-agentTemplate.yaml
            ls -la
            sed -i "s/^  //g" agentTemplate.yaml 
            sed -i '1d' agentTemplate.yaml 
            cat agentTemplate.yaml
            envsubst < agentTemplate.yaml > gen-agentTemplate.yaml
            ls -la            
        """
        //#sed -i '1d' tmp-podagent.yaml #workartund
        agentYaml = readYaml file: 'gen-agentTemplate.yaml'
        println "GEN-AGENT"
        println agentYaml
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false

        /* sh '''
            rm -v agent.yaml
         '''
         */
    }
    Map tmpAgent="""---
kind: Pod
metadata:
  name: maven
spec:
  containers:
    - name: maven
      image: ${config.build.maven.image}
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
    return tmpAgent
}


