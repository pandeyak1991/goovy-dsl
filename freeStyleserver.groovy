freeStyleJob("${FolderName}/server"){
    logRotator{
        daysToKeep(10)
        numToKeep(10)
    }
    wrappers{
        preBuildCleanup()
        credentialsBinding{
            usernamePassword{
            usernameVariable('GIT_USERNAME')
            passwordVariable('GIT_PASSWORD')
            credentialsId('JenkinsGithubApp')
        }
        }
        withFolderProperties()
    }
    steps{
        shell("""touch propsfile
        echo "MFG_DATE=\$(date '+%Y-%m-%d')" >>propsfile
        echo "SERVER_VERSION=${API_SERVER_VERSION}.\$BUILD_NUMBER" >> propsfile""")
        environmentVariables{ 
            propertiesFile("\$WORKSPACE/propsfile") //inejct propsfile content as env
        }
  
        shell("""
        echo \${SERVER_VERSION}""")
    }
    steps{
        downstreamParameterized{
            trigger("Common-jobs/common-docker-build-arm"){
                
                parameters{
                    predefinedProp("REPO_URL","\${GIT_URL}")
                    predefinedProp("COMMIT_ID","\${GIT_COMMIT}")
                    predefinedProp("MFG_DATE","\${MFG_DATE}")
                    predefinedProp("SERVER_VERSION","\${SERVER_VERSION}")
                    predefinedProp("MAIN_JOB_BUILD_NUMBER","\${BUILD_NUMBER}")
                }
            }

        }
        
    }

    
}