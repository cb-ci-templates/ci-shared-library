library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-shared-library.git'])
//Load the defaults
Map configMap = readYaml text: libraryResource("json/ci-config-defaults.yaml")
println configMap
pipeline_simple2 (configMap)