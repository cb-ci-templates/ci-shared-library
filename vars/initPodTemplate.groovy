def call(Map config) {
    def result=null
    container("envsubst") {
        //TODO: Iterate over all config.build.X.images and expose them as ebv vars
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        writeYaml file: "agent.yaml" , data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh '''
            ls -la 
            cat agent.yaml
            envsubst < agent.yaml > tmp-agent.yaml
            sed -i -e '1d'  tmp-agent.yaml 
            sed -i '/|/d' tmp-agent.yaml #workartund to remove "|"
            cat tmp-agent.yaml
        '''
        //writeYaml file: config.dynamicPodTemplateFile , data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        result = readYaml file: "tmp-agent.yaml"
        sh '''echo $result
           rm tmp-agent.yaml
        '''

    }
    return result
}
