library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-shared-library.git'])


// Building the data object
def configYaml = """---
app : 'Hello World'
k8_agent_yaml : 'podTemplate-curl.yaml'
param_greetings : 'Hello World'
"""
println "!!!!!!!!!!!!!!!"
Map configMap = readYaml text: "${configYaml}"
println "????????????"
pipelineHelloWorld (configMap)