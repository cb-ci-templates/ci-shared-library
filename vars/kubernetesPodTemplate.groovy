// vars/kubernetesPodTemplate.groovy
def call(Map config=[:]) {
    def template = libraryResource("podtemplates/${config.file}")
    /*TODO: iterate over images from ci-config
    if (config.binding) {
        def engine = new groovy.text.GStringTemplateEngine()
        template = engine.createTemplate(template).make(config.binding).toString()
    }
     */
    return template
}