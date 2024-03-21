freeStyleJob("${FolderName}/server"){
    logRotator{
        daysToKeep(10)
        numToKeep(10)
    }
    // wrappers{
    //     preBuildCleanup()
    //     // credentialsBinding{
    //     //     usernameVariable('GIT_USERNAME')
    //     //     passwordVariable('GIT_PASSWORD')
    //     //     credentialsId('JenkinsGIT')
    //     // }
    }
}