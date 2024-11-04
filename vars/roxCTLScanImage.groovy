package vars

def call(Map config=[:]) {
    withCredentials([string(credentialsId: 'roxctl-user-token', variable: 'API_TOKEN')]) {

        sh """
export ROX_API_TOKEN="${API_TOKEN}"
ROX_CENTRAL_ENDPOINT="${config.ENDPOINT}"
INSECURE_SKIP_TLS_VERIFY="${config.SKIP_TLS_VERIFY}"
IMAGE_REGISTRY="${config.IMAGE_REGISTRY}"
IMAGE_NAME="${config.IMAGE_NAME}"

curl -s -k -L -H "Authorization: Bearer ${env.ROX_API_TOKEN}" \\
  "https://${env.ROX_CENTRAL_ENDPOINT}/api/cli/download/roxctl-linux" \\
  --output ./roxctl  \\
  > /dev/null

chmod +x ./roxctl  > /dev/null

if [ "${env.INSECURE_SKIP_TLS_VERIFY}" = "true" ]; then
  export ROX_INSECURE_CLIENT_SKIP_TLS_VERIFY=true
fi

./roxctl image scan -e "${env.ROX_CENTRAL_ENDPOINT}" --image "${env.IMAGE_REGISTRY}/${env.IMAGE_NAME}"
        """
    }

}