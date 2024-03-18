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
    if  (new File(branchPropertiesFile).exist()){
        echo "branchPropertiesFile : ${branchPropertiesFile} exist on branch"
        return true
    }else {
        echo "branchPropertiesFile : ${branchPropertiesFile} doesn`t exist on branch"
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