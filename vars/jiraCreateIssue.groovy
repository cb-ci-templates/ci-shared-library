def call(String jiraToken, Map config=[:]) {
    config["jiraToken"]=jiraToken
    sh """
    set -x
    cat <<EOF> createIssue.json
    {
       "fields": {
          "project": {
             "key": "${config.JIRA_KEY}"
          },
          "summary": "${config.JIRA_SUMMARY}",
          "description": "${config.JIRA_DESCRIPTION}",
          "issuetype": {
             "name": "${config.JIRA_ISSUE_TYPE}"
          },
          "assignee": {
             "name": "assignee-username"
          }
       }
    }
    EOF
    cat createIssue.json
    curl -D- -u ${config.JIRA_TOKEN} -X POST --data @createIssue.json -H "Content-Type: application/json" ${config.JIRA_URL}/rest/api/2/issue
    """

}