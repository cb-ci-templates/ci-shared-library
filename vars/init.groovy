def call(Map configDefaults) {
    def result = null
    container("json-schema-validator") {
        validate(configDefaults)
        result = readYaml file: configDefaults.branchPropertiesFile
    }
    //Optional:merge with global  defaults
    container("yq") {
        writeYaml file: 'ci-config-defaults.yaml', data: libraryResource("json/ci-config-defaults.yaml")
        sh """            
            sed -i '/|-/d' ci-config-defaults.yaml
            cat ci-config-defaults.yaml
            sed -i '/|-/d' ${configDefaults.branchPropertiesFile} 
            cat ${configDefaults.branchPropertiesFile}                      
            yq eval-all 'select(fileIndex == 0) * select(fileIndex == 1)' ${configDefaults.branchPropertiesFile} ci-config-defaults.yaml > config-merged.yaml
            sed -i '/---/d' config-merged.yaml
            cat config-merged.yaml
        """
        result = readYaml file: "config-merged.yaml"
        archiveArtifacts artifacts: '*.yaml', followSymlinks: false
        return result
    }
}
