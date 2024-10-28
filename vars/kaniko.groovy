def call(config, image_tag, environment) {
    env.DOCKER_CONFIG = "/kaniko/.docker"
    def proxyUrl = (environment == 'prod') ? 'http://YOURPROXYPROD:3128' : 'http://YOURPROXYDEV:3128'
    container(name: 'kaniko', shell: '/busybox/sh') {
        withCredentials([file(credentialsId: "${CRED_DOCKER_CRED_ID}", variable: 'DOCKER_CONFIG_JSON_FILE')]) {
            sh label: 'kanikoPrepareConfig', script: '''
                cp ${DOCKER_CONFIG_JSON_FILE} ${DOCKER_CONFIG}/config.json
            '''
            echo "Deploy to  : ${config.build.kaniko.registry}/${config.build.kaniko.application_image}:${image_tag}"
            sh label: 'kanikoBuildAndPush', script: """              
                export https_proxy="${config.build.kaniko.https_proxy}"
                export no_proxy="${config.build.kaniko.no_proxy}"
                /kaniko/executor  --dockerfile Dockerfile --insecure --skip-tls-verify --cache=false  --context . \
                --destination ${config.build.kaniko.registry}/${config.build.kaniko.application_image}:${image_tag} \
                --destination ${config.build.kaniko.registry}/${config.build.kaniko.application_image}:latest \
                --destination ${config.build.kaniko.registry_deploy}/${config.build.kaniko.application_image}:${image_tag} #\
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