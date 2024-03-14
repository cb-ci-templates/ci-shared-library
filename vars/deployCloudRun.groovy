// vars/cloudRunDeploy.groovy

def call (Map config){
    echo "CLOUDRUN"
    echo "$config.deploykey1"

}

//TODO: need to be tested
def cloudRun(Map config) {
    container(name: "${config.container}") {
        if (config.deployType == "gke") {
            sh "gcloud beta run deploy ${config.serviceName} --image ${config.image} --platform gke --cluster ${config.clusterName} --cluster-location ${config.region} --namespace ${config.namespace}"
        } else if (config.deployType == "vmware") {
            sh "gcloud beta run deploy ${config.serviceName} --image ${config.image} --platform kubernetes --namespace ${config.namespace} --kubeconfig ${config.kubeconfig}"
        } else {
            sh "gcloud beta run deploy ${config.serviceName} --image ${config.image} --allow-unauthenticated --region ${config.region} --platform managed"
        }
    }
}