def call (Map defaults){
    def result=false
    if (params!=null){
        result=validateParameters(params)
    }
    result=validateTemplateGlobalSetting(defaults) && result
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