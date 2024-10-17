def call(Map config=[:]) {
    writeYaml file: 'createIssueTemplate.json', data: libraryResource("jira/createIssue.json")
    env.JIRA_KEY=config.JIRA_KEY
    env.JIRA_ISSUE_TYPE_NAME=config.JIRA_ISSUE_TYPE_NAME
    env.JIRA_DESCRIPTION=config.JIRA_DESCRIPTION
    env.JIRA_SUMMARY=config.JIRA_SUMMARY
    env.JIRA_URL=config.JIRA_URL
    env.JIRA_TOKEN==config.JIRA_TOKEN
    sh "envsubst < createIssueTemplate.json  > createIssue.json"
    def render = renderTemplate(rawBody,binding)
    sh('curl -D- -u $JIRA_CREDENTIALS -X POST --data createIssue.json -H "Content-Type: application/json" $JIRA_URL/rest/api/2/issue')
}