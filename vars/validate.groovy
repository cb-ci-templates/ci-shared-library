def call (Map defaults){
    def result=false
    if (params!=null){
        result=validateParameters(params)
    }
    if (defaults.branchPropertiesFile!=null){
        result=validateBranchPropertiesExist (defaults.branchPropertiesFile)
    }
    result=validateTemplateGlobalSetting(defaults) && result
    echo "validate ${result}"
    return result
}

def validateBranchPropertiesExist(String branchPropertiesFile){
    if  (new File("${defaults.branchPropertiesFile}").exist()){
        return true
    }else {
        return false
    }
}

def validateTemplateGlobalSetting(Map defaults){
    //TODO: Impl
    return true;
}

def validateParameters(Map params){
    //TODO: Impl
    return true;
}