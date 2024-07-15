def call(Map config) {
    def agentYaml=null
    container("envsubst") {
        //TODO: Iterate over all config.build.X.images and expose them as ebv vars
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        writeYaml file: 'agent.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            ls -la 
            cat agent.yaml
            envsubst < agent.yaml > ${config.dynamicPodTemplateFile}
            #sed -i '1d' ${config.dynamicPodTemplateFile} #workartund
            cat ${config.dynamicPodTemplateFile}
        """
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        agentYaml = readYaml file: config.dynamicPodTemplateFile
       /* sh '''
           rm -v agent.yaml
        '''
        */
    }
    return agentYaml
}
