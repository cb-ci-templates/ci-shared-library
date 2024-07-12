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


def initProperties(String propertyFileName) {
    try{
        //use the Pipeline Utility Steps plugin readProperties step to read the .<app>.properties custom marker file
        echo "INIT from Branchproperties: ${propertyFileName}"
        def props = readProperties defaults: parameters, file: propertyFileName
        //Set all properties to env
        for (e in props) {
            env.setProperty(e.key, e.value)
        }
        //Expose all parameters to env
        for (p in params) {
            echo "set parameter ${p.key}:${p.value} to environment"
            env.setProperty(p.key, p.value)
        }
    }catch(FileNotFoundException e){
        error"File ${propertyFileName} not found on branch"
    }
}
