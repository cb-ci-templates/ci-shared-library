def call (String file,Map defaults,Map params){
    def result=validateTemplateGlobalSetting(defaults) &&  validateParameters(params)
    echo "validate ${result}"
    return result
}


def validateTemplateGlobalSetting(Map defaults){
    //TODO: Impl
    return true;
}

def validateParameters(Map params){
    //TODO: Impl
    return true;
}