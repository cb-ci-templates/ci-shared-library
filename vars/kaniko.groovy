def call(config) {
    env.DOCKER_CONFIG = "/kaniko/.docker"
    container(name: 'kaniko', shell: '/busybox/sh') {
        withCredentials([file(credentialsId: 'dockerconfig', variable: 'DOCKER_CONFIG_JSON_FILE')]) {
            sh label: 'kanikoPrepareConfig', script: '''
                cp ${DOCKER_CONFIG_JSON_FILE} ${DOCKER_CONFIG}/config.json
            '''
            echo "Deploy to  : ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:${env.SHORT_COMMIT}"
            //expose SHORT_COMMIT
            gitShortCommit 7
            sh label: 'kanikoBuildAndPush', script: """              
                export https_proxy="${config.ci.https_proxy}"
                export no_proxy="${config.ci.no_proxy}"
                /kaniko/executor  --dockerfile Dockerfile --insecure --skip-tls-verify --cache=false  --context . \
                --destination ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:${env.SHORT_COMMIT} \
                --destination ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:latest #\

                #--destination ${config.ci.kaniko.registry_deploy}/${config.ci.kaniko.application_image}:${env.SHORT_COMMIT} #\
                #--build-arg HTTP_PROXY=${proxyUrl} \
                #--build-arg HTTPS_PROXY=${proxyUrl} \
                #--build-arg http_proxy=${proxyUrl} \
                #--build-arg https_proxy=${proxyUrl}
                #--build-arg NO_PROXY=gcr.io,*.gcr.io \
                #--build-arg no_proxy=gcr.io,*.gcr.io
             """

        }
    }
}