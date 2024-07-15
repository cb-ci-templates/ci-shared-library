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
                   .replace('\${MAVEN_IMAGE}',config.build.maven.image)
                   .replace('\${KANIKO_IMAGE}',config.build.kaniko.image)

        // Function to replace tokens using a map


// Replace tokens
        //agentPod = replaceTokens(agentPod, replacements)
        writeYaml file: podTemplateFilePath, data: agentPod
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false


        // Check if specific paths exist
        def path1 = ['build', 'maven', 'image']
        def path2 = ['build', 'maven', 'nonexistent']

        println "Path ${path1} exists: ${pathExists(agentPod, path1)}"
        println "Path ${path2} exists: ${pathExists(agentPod, path2)}"

        return agentPod
    }
}




// Function to check if a YAML path exists
def pathExists(Map<String, Object> map, List<String> path) {
    def current = map
    for (part in path) {
        if (current.containsKey(part)) {
            current = current[part]
        } else {
            return false
        }
    }
    return true
}



