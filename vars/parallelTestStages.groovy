// Convert array of regions to map of stages
def call(stages) {
        stageList=stages.collectEntries { mystage ->
            [
                    (mystage): {
                        stage("${mystage}") {
                            //TODO we need the test params here
                            echo  "sample test command ${mystage}"
                        }
                    }
            ]
        }
        parallel stageList
 }