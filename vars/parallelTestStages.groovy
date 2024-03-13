// Convert array of regions to map of stages
def call(stageList) {
    return stageList.collectEntries { stage ->
        [
                (stage): {
                    stage("${stage}") {
                        echo  "sample command ${stage}"
                    }
                }
        ]
    }
}