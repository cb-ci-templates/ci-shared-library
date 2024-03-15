def call (String file,Map defaults,map params){
    return alidatePropertyFile(file) &&  validateTemplateGlobalSetting(defaults) &&  validateParameters(params)

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