pipelineJob("Common-jobs/common-docker-build-arm"){
    logRotator{
        daysToKeep(10)
        numToKeep(10)
    }
    parameters{
        stringParam{
            name()
            defaultVaulue('')
            description('')
            trim(false)
        }
        stringParam{
            name('REPO_URL')
            defaultVaulue('')
            description('')
            trim(false)
        }
        stringParam{
            name('COMMIT_ID')
            defaultVaulue('')
            description('')
            trim(false)
        }
        stringParam{
            name('MFG_DATE')
            defaultVaulue('')
            description('')
            trim(false)
        }
        stringParam{
            name('SERVER_VERSION')
            defaultVaulue('')
            description('')
            trim(false)
        }
        stringParam{
            name('MAIN_JOB_BUILD_NUMBER')
            defaultVaulue('')
            description('')
            trim(false)
        }
    }
    properties{
        copyArtifactsPermission{
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