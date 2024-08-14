// vars/kubernetesPodTemplate.groovy
//see https://community.jenkins.io/t/pass-variables-into-a-podtemplate-loaded-from-shared-library/1655/2
def call(Map config) {
    def template = libraryResource("podtemplates/${config.dynamicPodTemplateFile}")

    /*TODO: iterate over images from ci-config
    if (config.binding) {
        def engine = new groovy.text.GStringTemplateEngine()
        template = engine.createTemplate(template).make(config.binding).toString()
    }
     */
    return template
}
