@Grab(group='org.yaml', module='snakeyaml', version='1.29')
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Paths

def call(String fileName){
    def myMap = [:]

    // Read YAML file
    def yamlText = new String(Files.readAllBytes(Paths.get(fileName)))

    // Parse YAML
    def templateYaml = new Yaml().load(yamlText)
    templateYaml.parameters.each { property ->
        //println("Name: ${property.name},DefaultValue: ${property.defaultValue}")
        myMap["$property.name"] = "${property.defaultValue}"
    }
    println(myMap)
    return myMap
}