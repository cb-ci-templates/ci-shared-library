def call(config) {
    //TODO: Check the path in ci-config if kaniko is enabled/defined
    if (config.ci.kaniko){
        buildKaniko config
    }
}