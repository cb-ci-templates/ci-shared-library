

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


        //Test code below doesnt work yet
        // Function to check if a YAML path exists
        def images=sh(script: '''
                set -x
                yq eval -o=json '.' agentTemplate.yaml |jq -r 'paths |join(".")'
                for img in \$(yq eval -o=json '.' agentTemplate.yaml |jq -r 'paths |join(".")' |grep -E "build.*.image")
                do
                    echo \$img
                done
            ''',returnStdout: true)
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










