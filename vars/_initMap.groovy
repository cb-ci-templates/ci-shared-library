//import groovy.yaml.YamlSlurper

def call(String fileName){
     // Read YAML file
    //def yamlSlurper = new YamlSlurper(new String(Files.readAllBytes(Paths.get(fileName))))
    sh "ls -lR"
    // Parse YAML content
    //def yamlMap = yamlSlurper.parseText(yamlText)
    /*node(){
        Map yamlMap = readYaml(file: fileName)
        return yamlMap
    }
    */


    // Print the Map
    println(yamlMap)



}