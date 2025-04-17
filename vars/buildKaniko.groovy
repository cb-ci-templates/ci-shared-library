def call (Map config) {
//jnlp has git installed
    container("jnlp") {
        //expose SHORT_COMMIT
        gitShortCommit 7
    }
    env.DOCKER_CONFIG = "/kaniko/.docker"
    container(name: 'kaniko', shell: '/busybox/sh') {
        //withCredentials([file(credentialsId: 'dockerconfig', variable: 'DOCKER_CONFIG_JSON_FILE')]) {
        withCredentials([string(credentialsId: 'dockerconfig', variable: 'DOCKER_CONFIG_JSON')]) {
            sh label: 'kanikoPrepareConfig', script: '''
                echo ${DOCKER_CONFIG_JSON} > ${DOCKER_CONFIG}/config.json
                cat ${DOCKER_CONFIG}/config.json
            '''
            echo "Deploy to  : ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:${SHORT_COMMIT}"
            def kanikoProxy=""
            if (env.HTTPS_PROXY) {
                kanikoProxy=" --build-arg HTTP_PROXY=${env.HTTPS_PROXY} --build-arg HTTPS_PROXY=${env.HTTPS_PROXY}  --build-arg http_proxy=${env.HTTPS_PROXY}  --build-arg https_proxy=${env.HTTPS_PROXY} "
                println kanikoProxy
            }
            sh label: 'kanikoBuildAndPush', script: """   
                env  |grep -i proxy |sort           
                export https_proxy="${env.https_proxy}"
                export no_proxy="${env.no_proxy}"
                env  |grep -i proxy |sort 
                /kaniko/executor  --dockerfile Dockerfile --insecure --skip-tls-verify --cache=false  --context . \
                --destination ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:${SHORT_COMMIT} \
                --destination ${config.ci.kaniko.registry}/${config.ci.kaniko.application_image}:latest #\
                # ${kanikoProxy} 
                #--destination ${config.ci.kaniko.registry_deploy}/${config.ci.kaniko.application_image}:${SHORT_COMMIT} #\
                #--build-arg HTTP_PROXY=${config.ci.https_proxy} \
                #--build-arg HTTPS_PROXY=${config.ci.https_proxy} \
                #--build-arg http_proxy=${config.ci.https_proxy} \
                #--build-arg https_proxy=${config.ci.https_proxy}
                #--build-arg NO_PROXY=gcr.io,*.gcr.io \
                #--build-arg no_proxy=gcr.io,*.gcr.io
             """

        }
    }
}