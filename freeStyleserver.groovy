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

        }
    }

    
}