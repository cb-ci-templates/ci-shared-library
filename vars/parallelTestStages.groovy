// Convert array of regions to map of stages
def call(stages) {
        def myStages=stages
        stageList=myStages.collectEntries { mystage ->
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