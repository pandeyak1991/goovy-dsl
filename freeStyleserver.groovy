freeStyleJob("${FolderName}/server"){
    logRotator{
        daysToKeep(10)
        numToKeep(10)
    }
}