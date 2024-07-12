library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-shared-library.git'])


// Building the data object for global setting
def configYaml = """---
app : 'Hello World'
branchPropertiesFile: 'ci-config.yaml'
"""

Map configMap = readYaml text: "${configYaml}"
println configMap
pipeline_simple (configMap)