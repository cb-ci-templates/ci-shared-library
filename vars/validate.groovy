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
    sh """if test -f "${branchPropertiesFile}"
          then
            echo \"${branchPropertiesFile} exists on branch.\"
          else
            echo \\"${branchPropertiesFile} does not exists on branch.\\"
          fi
    """
}

def validateTemplateGlobalSetting(Map defaults){
    //TODO: Impl
    return true;
}

def validateParameters(Map params){
    //TODO: Impl
    return true;
}