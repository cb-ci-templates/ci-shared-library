
def call(String configFileName) {
    try {
        //Load config
        valuesYaml = readYaml(file: "${configFileName}")

        //generate properties, global options and parameters
        //see https://docs.cloudbees.com/docs/cloudbees-ci/latest/automating-with-jenkinsfile/customizing-parameters
        evaluate(valuesYaml.properties)

        //generate global environment vars
        valuesYaml.environment.each { environmentVar ->
            evaluate("env."+environmentVar)
        }

    } catch (Exception e) {
        println "FileNotFound ${configFileName}"
    }
}