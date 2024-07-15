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
            envsubst < agentTemplate.yaml |yq > gen-agentTemplate.yaml
            cat gen-agentTemplate.yaml
            ls -la            
        """
        //#sed -i '1d' tmp-podagent.yaml #workartund
        agentYaml = readYaml file: 'gen-agentTemplate.yaml'
        //println "GEN-AGENT"
        //println agentYaml
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false

        /* sh '''
            rm -v agent.yaml
         '''
         */
    }
    return agentYaml
}


