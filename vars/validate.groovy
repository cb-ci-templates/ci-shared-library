def call (String file,Map defaults,Map params){
    def result= validatePropertyFile(file) &&  validateTemplateGlobalSetting(defaults) &&  validateParameters(params)
    echo "validate ${result}"
    return result
}
def validatePropertyFile(String file){
    //TODO: Impl: Can we use scheme validation?
    return true;
}

def validateTemplateGlobalSetting(Map defaults){
    //TODO: Impl
    return true;
}

def validateParameters(Map params){
    //TODO: Impl
    return true;
}