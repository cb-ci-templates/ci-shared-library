

def call(Map config) {
    def result=null
    container("yq") {
         // Define the path to your template file
        def podTemplateFilePath = 'agentTemplate.yaml'
        def agentPod=libraryResource("podtemplates/podTemplate-envsubst-images.yaml")
                   .replace('\${MAVEN_IMAGE}',config.ci.maven.image)
                   .replace('\${KANIKO_IMAGE}',config.ci.kaniko.image)
        writeYaml file: podTemplateFilePath , data: agentPod
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        return agentPod
    }
}










