// Convert array of regions to map of stages
def call(stageList) {
    stages=stageList.collectEntries { stage ->
        [
                (stage): {
                    stage("${stage}") {
                        echo  "sample command ${stage}"
                    }
                }
        ]
    }
    parallel stages
}