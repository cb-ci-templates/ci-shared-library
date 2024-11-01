package vars

def call(Map config=[:]) {
    withCredentials([usernamePassword(credentialsId: 'jfrog-user-token', variable: 'API_TOKEN')]) {

        sh """\
            export FILE_PATH=${config.FILE_PATH}
            export ARTIFACTORY_URL=${config.ARTIFACTORY_URL}
            export REPO_NAME=${config.REPO_NAME}
            export ARTIFACT_PATH=${config.ARTIFACT_PATH}
            
            curl -H "Authorization: Bearer ${API_TOKEN}" -T ${FILE_PATH} "${ARTIFACTORY_URL}/artifactory/${REPO_NAME}/${ARTIFACT_PATH}"
        """.stripIndent()
    }

}