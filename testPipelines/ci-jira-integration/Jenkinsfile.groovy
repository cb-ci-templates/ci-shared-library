library identifier: 'ci-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-shared-library.git'])

// Building the data object
def configYaml = """---
JIRA_KEY : 'SCRUM'
JIRA_ISSUE_TYPE : 'Task'
JIRA_DESCRIPTION: 'TEST1'
JIRA_SUMMARY: 'TEST2'
JIRA_URL: 'https://cloudbees-acaternberg-test.atlassian.net/'
"""
Map configMap = readYaml text: "${configYaml}"
println configMap
pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: shell
      image: curlimages/curl:latest
      runAsUser: 1000
      command:
        - cat
      tty: true
      workingDir: "/home/jenkins/agent"
      securityContext:
        runAsUser: 1000
'''
            defaultContainer 'shell'
            retries 2
        }
    }
    stages {
        stage('Main') {
            steps {
                container ("shell"){
                    jiraComment body: 'This is my test comment1', issueKey: configMap.JIRA_KEY
                    jiraCreateIssue configMap
                    //step([$class: 'JiraVersionCreatorBuilder', jiraProjectKey: "${configMap.JIRA_KEY}", jiraVersion: 'newversion'])
                }
            }
        }
    }
}
