//import groovy.yaml.YamlSlurper

def call(String fileName){
     // Read YAML file
    //def yamlSlurper = new YamlSlurper(new String(Files.readAllBytes(Paths.get(fileName))))

    // Parse YAML content
    //def yamlMap = yamlSlurper.parseText(yamlText)
    Map yamlMap = readYaml(file: fileName)
    // Print the Map
    println(yamlMap)


    return yamlMap
}