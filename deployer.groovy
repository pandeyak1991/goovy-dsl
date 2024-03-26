pipelineJobs("Hyblock-nprod-Master/internal-jobs/deployer"){
    logRotator{
        daysToKeep(10)
        numToKeep(10)
    }
    parameters{
        stringParam{
            name('REPO_URL')
            defaultValue('')
            description('')
            trim(false)
        }
        stringParam{
            name('COMMIT_ID')
            defaultValue('')
            description('')
            trim(false)
        }
        stringParam{
            name('MFG_DATE')
            defaultValue('')
            description('')
            trim(false)
        }    
        stringParam{
            name('JobName')
            defaultValue('')
            description('')
            trim(false)
        }
        stringParam{
            name('BUILDNO')
            defaultValue('')
            description('')
            trim(false)
        }

        stringParam{
            name('SERVER_VERSION')
            defaultValue('')
            description('')
            trim(false)
        }
        stringParam{
            name('MAIN_JOB_BUILD_NUMBER')
            defaultValue('')
            description('')
            trim(false)
        }
    }
    definition{
        cpsFlowDefinition{
            sandbox(true)
            script('''
            node{
                stage('Copy-Artifacts'){
                    copyArtifacts filter: "**/*.txt"
                }
                stage('ls file'){
                    sh 'ls -l'
                }
            }'''
            
            )
        }
    }

}