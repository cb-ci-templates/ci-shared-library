// vars/defineProps.groovy
def call(String file, Map defaults) {
  //use the Pipeline Utility Steps plugin readProperties step to read the .<app>.properties custom marker file
  def props = readProperties defaults: defaults, file: file
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
