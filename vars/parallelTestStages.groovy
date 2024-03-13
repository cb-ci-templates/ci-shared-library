// Convert array of regions to map of stages
def call(stages) {
        //TODO: we need a 2 dimensional array
        //[stage1][testparams1]
        //[stageX][testparamsX]
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