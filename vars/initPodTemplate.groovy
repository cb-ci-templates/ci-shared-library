def call(Map config) {
    def agentYaml=null
    container("envsubst") {
        //TODO: Iterate over all config.build.X.images and expose them as ebv vars
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        agentRef=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        //writeYaml file: 'agentTemplate.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            echo ${agentRef} |envsubst > gen-agentTemplate.yaml
            ls -la
            sed -i "s/^  //g" agentTemplate.yaml 
            sed -i '1d' agentTemplate.yaml 
            cat agentTemplate.yaml
            envsubst < agentTemplate.yaml > gen-agentTemplate.yaml
            ls -la            
         """
        //#sed -i '1d' tmp-podagent.yaml #workartund
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
       // agentYaml = readYaml file: 'gen-agentTemplate.yaml'
    }
    container ("yq"){
        sh "yq gen-agentTemplate.yaml"
        agentYaml = readYaml file: 'gen-agentTemplate.yaml'
        println agentYaml
       /* sh '''
           rm -v agent.yaml
        '''
        */
    }
    return agentYaml
}
