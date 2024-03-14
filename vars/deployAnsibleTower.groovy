def call(Map params) {
    //see https://github.com/goetzrieger/ansible-labs/blob/master/tower/ansible_tower_advanced.adoc
    //TODO: use parameters
     return  ansibleTower(
            credential: params.crdentialsID,
            extraVars: '''{                        
                                  extra_vars: {                        
                                    KEY1: "VAL1",                        
                                    KEY2: "enabled",                        
                                    KEY3: "VAL3"                        
                                  }                        
                                }''',
            inventory: params.host,
            jobTemplate: params.jobID,
            jobType: 'run',
            throwExceptionWhenFail: false,
            towerCredentialsId: params.towerCredentialsID,
            towerLogLevel: 'false',
            towerServer: params.ansibletowername //references manage jenkins -> configure system -> ansible tower setup
    )
}
