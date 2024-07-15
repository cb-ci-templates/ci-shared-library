

def call(Map config) {
    def result=null
    container("yq") {
         // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        def agentPod=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
                   .replace('\${MAVEN_IMAGE}',config.build.maven.image)
                   .replace('\${KANIKO_IMAGE}',config.build.kaniko.image)

        // Function to replace tokens using a map
        //agentPod = replaceTokens(agentPod, replacements)
        writeYaml file: podTemplateFilePath , data: agentPod
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        // Function to check if a YAML path exists
        def images=sh(script: """
                yq -o=json '.' ${podTemplateFilePath} |jq -r 'paths |join(".")' |grep -E "build.*.image" |tr "\\n" ","
            """,returnStdout: true)
        println "IMAGES"
        println images
        images.each {image ->
            println image.toUpperCase()
            tmpImage=image.toUpperCase().replace(".","_")
            println tmpImage
            agentPod.replace("\${" + tmpImage + "}","tmpImage")
        }

        return agentPod
    }
}










