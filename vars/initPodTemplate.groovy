def call(Map config) {
    def result=null
    container("yq") {
        // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        env.MAVEN_IMAGE = config.build.maven.image
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        //agentRef=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        writeYaml file: podTemplateFilePath, data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        sh """
            ls -la
            sed -i "s/^  //g" ${podTemplateFilePath}
            sed -i '1d' ${podTemplateFilePath}
            cat ${podTemplateFilePath}
            envsubst < ${podTemplateFilePath} |yq > gen-agentTemplate.yaml
            cat gen-agentTemplate.yaml
            diff -u gen-agentTemplate.yaml ${podTemplateFilePath}
            ls -la            
        """
        result = sh(returnStdout: true, script: "yq gen-agentTemplate.yaml")

        //#sed -i '1d' tmp-podagent.yaml #workartund
        //result = readYaml file: 'gen-agentTemplate.yaml'
        echo "ECHO"
        echo "${result.metadata.name}"
        //println "GEN-AGENT"
        //println result
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false

        /* sh "rm -v ${podTemplateFilePath}"
         */
    }
    return result
}


