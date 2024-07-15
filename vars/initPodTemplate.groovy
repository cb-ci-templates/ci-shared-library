def call(Map config) {
    def agentYaml=null
    container("envsubst") {
        //TODO: Iterate over all config.build.X.images and expose them as ebv vars
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        writeYaml file: 'agent.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh '''
            ls -la 
            cat agent.yaml
            envsubst < agent.yaml > tmp-podagent.yaml
            sed -i '1d' tmp-podagent.yaml #workartund
            cat tmp-podagent.yaml
        '''
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        agentYaml = readYaml file: "tmp-podagent.yaml"
       /* sh '''
           rm -v tmp-podagent.yaml
           rm -v agent.yaml
        '''
        */
    }
    return agentYaml
}
