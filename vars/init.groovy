// vars/defineProps.groovy
def call(Map config) {
    //Init from yaml. It uses the `readYaml` step which can not use defaults
    //initFromYaml "./ci-config.yaml"
    echo("${config}")
    validate(config)
    //init from properties with defaults  (here "default_key1" f.e)
    //initProperties(parameters.branchPropertiesFile)
    return initYaml(config.branchPropertiesFile)
}

def initYaml(String propertyFileName) {
    def configYaml = readYaml file: propertyFileName
    return configYaml
}
