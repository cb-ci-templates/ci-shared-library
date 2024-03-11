library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/org-caternberg/custom-sharedlib.git'])


// Building the data object
def configYaml = """---
app:'Hello World'
k8_agent_yaml:'podTemplate-curl.yaml'
param_greetings:'Hello World'
"""
Map configMap = readYaml text: "${configYaml}"

pipelineHelloWorld (app:"HelloWorld",k8_agent_yaml:'podTemplate-curl.yaml',param_greetings:'Hello World')
//pipelineHelloWorld (configMap)