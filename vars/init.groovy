def call(Map configDefaults) {
    def result=null
    container("json-schema-validator") {
        validate(config)
        script {
            result = readYaml file: configDefaults.propertyFileName
            env.MAVEN_IMAGE = result.build.maven.image
            //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
            writeYaml file: 'agent.yaml', data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        }
    }
    //Optional:merge with global  defaults
    container("yq") {
        writeYaml file: 'ci-config-defaults.yaml', data: libraryResource("json/ci-config-defaults.yaml")
        sh """
                            cat ci-config-defaults.yaml
                            cat ${configDefaults.branchPropertiesFile}                      
                            yq eval ${configDefaults.branchPropertiesFile} ci-config-defaults.yaml > config-merged.yaml
                            sed -i '/---/d' config-merged.yaml
                            cat config-merged.yaml
                        """
        script {
            result = readYaml file: "config-merged.yaml"
        }
        return result
    }
}
