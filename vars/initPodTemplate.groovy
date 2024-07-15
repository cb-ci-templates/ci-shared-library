def call(Map config) {
    Map agentYaml=null
    container("yq") {
        // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        //env.MAVEN_IMAGE="maven:3-amazoncorretto-17"
        //agentRef=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        writeYaml file: podTemplateFilePath, data: libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        // Define the environment variables
        def envVars = [
                MAVEN_IMAGE = config.build.maven.image
        ]
     /* TOD=: itearet over configv images and add to array
        def envVars = [
                MAVEN_IMAGE = config.build.maven.image,
                GRADLE_IMAGE = config.build.maven.image
        ]

      */






// Replace variables in the file
         agentYaml = replaceVariablesInFile(podTemplateFilePath, envVars)

// Print the result or save it back to a file
        println(agentYaml)
        // Optionally, save the result to a new file
// new File('output.yaml').text = result
        writeYaml file: 'gen-agentTemplate.yaml', data: agentYaml









       /* sh """
            #cat agentTemplate.yaml |envsubst |yq > gen-agentTemplate.yaml
            ls -la
            sed -i "s/^  //g" agentTemplate.yaml 
            sed -i '1d' agentTemplate.yaml 
            cat agentTemplate.yaml
            envsubst < agentTemplate.yaml > gen-agentTemplate.yaml
            ls -la            
         """

        */
        //#sed -i '1d' tmp-podagent.yaml #workartund
        agentYaml = readYaml file: 'gen-agentTemplate.yaml'
        println agentYaml
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false

        /* sh '''
            rm -v agent.yaml
         '''
         */
    }
    return agentYaml
}


def replaceVariablesInFile(filePath, envVars) {
    // Read the content of the file
    def fileContent = readYaml file: filePath
    // Replace placeholders with environment variables
    envVars.each { key, value ->
        fileContent = fileContent.replace("\${${key}}", value)
    }
    return fileContent
}
