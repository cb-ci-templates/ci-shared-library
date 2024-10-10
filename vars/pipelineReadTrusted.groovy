def call(Map config) {
    podTemplate(containers: [
            containerTemplate(name: 'shell', image: 'busybox', command: 'sleep', args: '99d')
    ]) {
        node(POD_LABEL) {

            /**
             Pre exec stage with branch decision
             */
            stage('Injected-pre-stage') {
                container('shell') {
                    echo 'Implement your pre-execution steps below......'
                    /*
                Implement your pre-execution steps here......
                */


                    if (env.BRANCH_NAME == 'master') {
                        echo 'I only execute on the master branch'
                    } else if (env.BRANCH_NAME.startsWith('feature')) {
                        echo "I execute on $BRANCH_NAME branch"
                    } else {
                        echo "I execute elsewhere"
                    }
                }
            }
            /**
             Here we inject the Jenkinsfile from the developer repo branch
             */
            evaluate(readTrusted("Jenkinsfile"))

            /**
             Post exec stage
             */
            stage('Injected-post-stage') {
                container('shell') {
                    echo 'Implement your post-execution steps below......'
                    /*
                    Implement your post-execution steps here......
                    E.g QA scans that should be executed in each Pipeline run
                */
                }
            }
        }
    }
}