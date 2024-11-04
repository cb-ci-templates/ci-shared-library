package vars

def call(Map config=[:]) {
    withCredentials([string(credentialsId: 'roxctl-user-token', variable: 'ROX_API_TOKEN')]) {
        def arch = sh(script: '''
arch="$(uname -m | sed "s/x86_64//" | sed "s/aarch/arm/")"
arch="${arch:+-$arch}"
echo "$arch"
    ''', returnStdout: true).trim()

        sh """
#curl -L -f -o roxctl "https://mirror.openshift.com/pub/rhacs/assets/4.5.4/bin/Linux/roxctl${arch}"

curl -s -k -L -H "Authorization: Bearer ${ROX_API_TOKEN}" \\
  "https://${config.ENDPOINT}/api/cli/download/roxctl-linux${arch}" \\
  --output ./roxctl  \\
  > /dev/null

chmod +x ./roxctl  > /dev/null

if [ "${config.SKIP_TLS_VERIFY}" = "true" ]; then
  export ROX_INSECURE_CLIENT_SKIP_TLS_VERIFY=true
fi

./roxctl version

./roxctl image scan -e "${config.ENDPOINT}" --image "${config.IMAGE_REGISTRY}/${config.IMAGE_NAME}"
        """
    }
}