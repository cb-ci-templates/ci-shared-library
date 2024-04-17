def call(Map params){
    def newSemanticVersionScript = libraryResource 'scripts/newSemanticVersion.sh'
    writeFile encoding: 'utf-8', file: 'nsv.sh', text: newSemanticVersionScript
    newSemanticVersion=sh(script: """
                               nsv.sh ${params}
                            """
                            ,returnStdout: true)
    echo "New Version: ${newSemanticVersion}"
    env.NEW_SEMANTIC_VERSION="${newSemanticVersion}"
    return newSemanticVersion
}