def call(Map config) {
    def agentYaml=null
    container("envsubst") {
        //TODO: Iterate over all config.build.X.images and expose them as ebv vars
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        writeYaml file: 'agentTemplate.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            ls -la 
            cat agentTemplate.yaml
            envsubst <agentTemplate.yaml> agentTemplate.yaml
            sed -i '1d' agentTemplate.yaml
            sed -i "s/^  //g" agentTemplate.yaml
            ls -la            
         """
        //#sed -i '1d' tmp-podagent.yaml #workartund
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        agentYaml = readYaml file: 'agentTemplate.yaml'
    }
    container ("yq"){
        sh "yq eval agentTemplate.yaml  > agentTemplate.yaml"
        agentYaml = readYaml file: 'agentTemplate.yaml'
        println agentYaml
       /* sh '''
           rm -v agent.yaml
        '''
        */
    }
    return agentYaml
}
