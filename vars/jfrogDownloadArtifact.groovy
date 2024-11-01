package vars

def call(Map config=[:]) {
    withCredentials([string(credentialsId: 'jfrog-user-token', variable: 'API_TOKEN')]) {

        sh """
            export ARTIFACTORY_URL=${config.ARTIFACTORY_URL}
            export REPO_NAME=${config.REPO_NAME}
            export ARTIFACT_PATH=${config.ARTIFACT_PATH}
            
            curl -H "Authorization: Bearer ${API_TOKEN}" -O "${ARTIFACTORY_URL}/artifactory/${REPO_NAME}/${ARTIFACT_PATH}"
        """
        archiveArtifacts artifacts: '*.*', followSymlinks: false
    }

}