def call(String jiraToken, Map config=[:]) {
    writeYaml file: 'jiraCreateIssue.sh', data: libraryResource("jira/createIssue.sh")
    config["jiraToken"]=jiraToken
    writeYaml file: 'jiraCreateIssueIN.json', data: config
    sh "cat jiraCreateIssueIN.json"
    sh """
     chmod 755 ./jiraCreateIssue.sh
     ls -la
     ./jiraCreateIssue.sh \
        '${config.JIRA_KEY}' \
        '${config.JIRA_ISSUE_TYPE}' \
        '${config.JIRA_DESCRIPTION}' \
        '${config.JIRA_SUMMARY}' \
        '${config.JIRA_URL}' \
        '${config.jiraToken}'
    """
}