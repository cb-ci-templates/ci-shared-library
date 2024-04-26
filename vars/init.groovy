// vars/defineProps.groovy
def call(Map parameters) {
  //Init from yaml. It uses the `readYaml` step which can not use defaults
  //initFromYaml "./ci-config.yaml"
   validate(pipelineParams)
  //init from properties with defaults  (here "default_key1" f.e)
    init(pipelineParams)

    echo """
    ###### SAMPLE OUTPUT OF VARS#####
    Pipeline parameter: ${params.greeting}
    Pipeline Template parameter: ${pipelineParams.app}
    default_key1 ${env.default_key1}
    branch_key1 ${env.key1}
    property DEPLOY from default config:${env.deploy}
    ###### END SAMPLE OUTPUT OF VARS#####
    """
}

def init(Map parameters) {
  //use the Pipeline Utility Steps plugin readProperties step to read the .<app>.properties custom marker file
  echo "INIT from Branchproperties: ${parameters.branchPropertiesFile}"
  def props = readProperties defaults: parameters, file: parameters.branchPropertiesFile
  //Set all properties to env
  for ( e in props ) {
    env.setProperty(e.key, e.value)
  }
  //Expose all parameters to env
  for (p in params) {
    echo "set parameter ${p.key}:${p.value} to environment"
    env.setProperty(p.key, p.value)
  }
}
