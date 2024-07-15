def call(Map config) {
    def result=null
    container("yq") {
        def replacements = [
                '\${MAVEN_IMAGE}': config.build.maven.image ?: 'maven:3-amazoncorretto-17',
                '\${KANIKO_IMAGE}': config.build.kaniko.image ?: 'kaniko'
        ]
        // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        def agentPod=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
        /*   .replace('\${MAVEN_IMAGE}',config.build.maven.image)
                .replace('\${GRADLE_IMAGE}',config.build.maven.image)
*/
        // Function to replace tokens using a map


// Replace tokens
        agentPod = replaceTokens(agentPod, replacements)
        writeYaml file: podTemplateFilePath, data: agentPod
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        return agentPod
    }
}

def replaceTokens (string,replacements) {
    replacements.each { token, value ->
        string = string.replace(token, value)
    }
    return string
}

