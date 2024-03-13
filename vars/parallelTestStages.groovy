// Convert array of regions to map of stages
def call(stages) {
        stageList=stages.collectEntries { mystage ->
            [
                    (mystage): {
                        stage("Deploy region ${mystage}") {
                            echo  "sample test command ${mystage}"
                        }
                    }
            ]
        }
        parallel stageList
 }