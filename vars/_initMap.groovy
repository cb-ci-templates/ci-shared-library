import groovy.io.FileType

def call(String fileName){
     // Read YAML file
    //def yamlSlurper = new YamlSlurper(new String(Files.readAllBytes(Paths.get(fileName))))
    //sh "ls -lR"
    // Parse YAML content
    //def yamlMap = yamlSlurper.parseText(yamlText)
    /*node(){
        Map yamlMap = readYaml(file: fileName)
        return yamlMap
    }
    */


    // Print the Map
    //println(yamlMap)

    def list = []

    def dir = new File(".")
    dir.eachFileRecurse (FileType.FILES) { file ->
        list << file
    }
    list.each {
        println it.path
    }


}