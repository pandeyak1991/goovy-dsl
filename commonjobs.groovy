pipelineJob("Common-jobs/common-docker-build-arm"){
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
    properties{
        copyArtifactPermission{
            projectNames("*")
        }
    }
    definition{
        cpsFlowDefinition{
            sandbox(true)
            script('''
            node{
                stage('Git-Pull'){
                    cleanWs()
                     git branch: 'main', url: 'https://github.com/pandeyak1991/goovy-dsl.git'
                }
                stage('Docker-build'){
                    echo "docker build " >file.txt
                    echo \${BUILD_NUMBER} >> file.txt
                }
                stage('Archiving Artifacts'){
                    archiveArtifacts '*.txt'
                }
            }
            ''')
        }
    }




}