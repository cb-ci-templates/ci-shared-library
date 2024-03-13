def call(Map params) {
    //see https://github.com/goetzrieger/ansible-labs/blob/master/tower/ansible_tower_advanced.adoc
     return  ansibleTower(
            credential: '<CREDENTIAL_ID>',
            extraVars: '''{                        
                                  extra_vars: {                        
                                    KEY1: "VAL1",                        
                                    KEY2: "enabled",                        
                                    KEY3: "VAL3"                        
                                  }                        
                                }''',
            inventory: 'localhost',
            jobTemplate: '146',
            jobType: 'run',
            throwExceptionWhenFail: false,
            towerCredentialsId: '<TOWER_CREDENTIAL>',
            towerLogLevel: 'false',
            towerServer: '<ANSIBLE_TOWER_NAME>' //references manage jenkins -> configure system -> ansible tower setup
    )
}
