// vars/kubernetesPodTemplate.groovy
def call(Map config=[:]) {
    def template = libraryResource("podtemplates/${config.file}")
    if (config.binding) {
        def engine = new groovy.text.GStringTemplateEngine()
        template = engine.createTemplate(template).make(config.binding).toString()
    }
    return template
}