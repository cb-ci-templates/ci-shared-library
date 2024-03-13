def call(stageList) {
    // Create an empty map to store parallel stages
    def parallelStagesMap = [:]

    // Iterate over the list of dynamic stages
    stageList.each { stageName ->
        // Add each stage to the parallel stages map
        parallelStagesMap[stageName] = {
            stage(stageName) {
                steps {
                    // Define the steps for each dynamic stage
                    echo "Executing ${stageName}"
                    // Add your actual build steps here
                }
            }
        }
    }

    def parallelBranches = stageList.collectEntries { n ->
        [(n): {
            stage(n) {
                steps {
                    sh "sleep 10"
                    echo "Done"
                }
            }
        }]
    }

    // Execute parallel stages
    parallel parallelBranches
}