
def call(String configFileName) {
    try {
        //Load config
        valuesYaml = readYaml(file: "./${configFileName}")

        //generate properties, global options and parameters
        //see https://docs.cloudbees.com/docs/cloudbees-ci/latest/automating-with-jenkinsfile/customizing-parameters
        evaluate(valuesYaml.properties)
    } catch (Exception e) {
        println "FileNotFound './ci.yaml'"
    }
}