// vars/kubernetesPodTemplate.groovy
def call(Map config,file) {
    def template = libraryResource("podtemplates/${file}")
    /*TODO: iterate over images from ci-config
    if (config.binding) {
        def engine = new groovy.text.GStringTemplateEngine()
        template = engine.createTemplate(template).make(config.binding).toString()
    }
     */
    return template
}