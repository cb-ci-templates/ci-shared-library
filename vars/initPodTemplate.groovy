def pathExists(Map yaml, List<String> path) {
    def current = yaml
    for (part in path) {
        if (current.containsKey(part)) {
            current = current[part]
        } else {
            return false
        }
    }
    return true
}

def call(Map config) {
    def result=null
    container("yq") {
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
        // Function to check if a YAML path exists


        // Check if specific paths exist
        def path1 = ['build', 'maven', 'image']
        def path2 = ['build', 'maven', 'nonexistent']

        this.pathExists(readYaml(text: "${agentPod}"), path1)
        this.pathExists(readYaml(text: "${agentPod}"), path2)

        return agentPod
    }
}










