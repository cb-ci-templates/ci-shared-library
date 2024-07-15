def call(Map config) {
    def result=null
    container("yq") {
        // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        //agentRef=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        //writeYaml file: podTemplateFilePath, data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        def agentPod=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            ls -la
            #sed -i "s/^  //g" ${podTemplateFilePath}
            #sed -i '1d' ${podTemplateFilePath}
            #cat ${podTemplateFilePath}
            echo ${agentPod}
            echo ${agentPod} |envsubst > gen-agentTemplate.yaml
            cat gen-agentTemplate.yaml            
        """
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        //return readYaml(file: "gen-agentTemplate.yaml").toString()
        result = sh(returnStdout: true, script: "yq gen-agentTemplate.yaml")
        return result
        //#sed -i '1d' tmp-podagent.yaml #workartund
        //result = readYaml file: 'gen-agentTemplate.yaml'

        //println "GEN-AGENT"
        //println result

        /* sh "rm -v ${podTemplateFilePath}"
         */
    }

}


