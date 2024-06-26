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
        downstreamParameterized{   // Triggering another and sending parameters
            trigger("Common-jobs/common-docker-builder-arm"){
                block{
                    buildStepFailure('UNSTABLE')
                    unstable('UNSTABLE')
                    failure('UNSTABLE')
                }
                
                parameters{
                    predefinedProp("REPO_URL","\${GIT_URL}")
                    predefinedProp("COMMIT_ID","\${GIT_COMMIT}")
                    predefinedProp("MFG_DATE","\${MFG_DATE}")
                    predefinedProp("SERVER_VERSION","\${SERVER_VERSION}")
                    predefinedProp("MAIN_JOB_BUILD_NUMBER","\${BUILD_NUMBER}")
                }
            }
        }
        copyArtifacts{  //Copying artifacts from above triggered job
            projectName("Common-jobs/common-docker-builder-arm")
            fingerprintArtifacts(true)
            selector{
                specific{
                    buildNumber("\$TRIGGERED_BUILD_NUMBER_Common_jobs_common_docker_builder_arm")
                }
            }
        }
    }
        publishers {  //Publishing artifact copied from triggerd job aboved
            archiveArtifacts('**/*.txt')
                downstreamParameterized{ // Triggering downstream job  when above jobs is success/stable
                    trigger("Common-jobs/common-docker-builder-arm"){
                        condition('SUCCESS')
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
        properties{
            copyArtifactPermission{
                projectNames("Hyblock-nprod-Master/internal-jobs/deployer")
            }
            promotions{
                def List list_of_region="${LIST_OF_REGIONS}".split(',').collect{it as String}
                def List list_of_icons="${LIST_OF_ICONS}".split(',').collect{it as String}
                for (x =0 ;x <list_of_region.size(); x++){
                    List branches=[
                       [ env_name:"${ENV1}",promote:"DEPLOY_TO_${ENV1}_${list_of_region[x]}",star:"star-${list_of_icons[x]}-e"],
                       [ env_name:"${ENV2}",promote:"DEPLOY_TO_${ENV2}_${list_of_region[x]}",star:"star-${list_of_icons[x]}-w",upstream: "DEPLOY_TO_${ENV1}_${list_of_region[x]}" ]

                    ]
                    branches.each { branch ->
                    if (branch.i <=("$NO_OF_ENV".toInteger())){
                        promotion{
                            name(branch.promote)
                            icon(branch.star)
                            conditions{
                                manual(branch.promoter){
                                    println "$RELEASE_ENVIRONMENT_NAME"
                                    if (branch.env_name == "$RELEASE_ENVIRONMENT_NAME" ){
                                        parameters{
                                            textParam("Approve Message","","Provide a Reason")
                                        }
                                    }
                                }
                                if (branch.i!=1){
                                    upstream(branch.upstream)
                                }
                            }
                            wrappers{
                                timestamps()
                                withFolderProperties()
                            }
                            actions{
                                downstreamParameterized{
                                    trigger("internal-jobs/deployer"){
                                        block{
                                            buildStepFailure('FAILURE')
                                            failure('FAILURE')
                                            unstable('UNSTABLE')
                                        }
                                        parameters{
                                            predefinedProp("JobName","\${PROMOTED_JOB_NAME}")
                                            predefinedProp("BUILDNO","\${PROMOTED_NUMBER}")
                                            predefinedProp("PromotedBuild_NUMBER","\${PROMOTED_NUMBER}")
                                        }
                                    }
                                }
                                downstreamParameterized{
                                    trigger("common-jobs/build-forever"){
                                      block{
                                        buildStepFailure('FAILURE')
                                        failure('FAILURE')
                                        unstable('UNSTABLE')
                                        }
                                        parameters{
                                        predefinedProp("JOB",'\$PROMOTED_JOB_NAME')
                                        predefinedProp("BUILDNO",'\$PROMOTED_NUMBER')
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}