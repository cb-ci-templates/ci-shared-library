set -x

export ROX_API_TOKEN="xxxx.token.xxxx"
ROX_CENTRAL_ENDPOINT="xxxx.central.xxxx"
INSECURE_SKIP_TLS_VERIFY="true"
IMAGE_REGISTRY="docker.io"
IMAGE_NAME="curlimages/curl:latest"

arch="$(uname -m | sed "s/x86_64//" | sed "s/aarch/arm/")"; arch="${arch:+-$arch}"
curl -s -k -L -H "Authorization: Bearer ${ROX_API_TOKEN}" \
  "https://$ROX_CENTRAL_ENDPOINT/api/cli/download/roxctl-linux${arch}" \
  --output ./roxctl  \
  > /dev/null

chmod +x ./roxctl  > /dev/null

./roxctl version

if [ "${INSECURE_SKIP_TLS_VERIFY}" = "true" ]; then
  export ROX_INSECURE_CLIENT_SKIP_TLS_VERIFY=true
fi

./roxctl image scan -e "$ROX_CENTRAL_ENDPOINT" --image "${IMAGE_REGISTRY}/${IMAGE_NAME}"
