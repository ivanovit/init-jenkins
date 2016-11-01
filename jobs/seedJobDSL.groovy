def multibranchPipelineJobName = 'Fusion';

multibranchPipelineJob(multibranchPipelineJobName) {
    branchSources {
        branchSource {
            source {
                gitHubSCMSource {
                    repoOwner('ivanovit')
                    repository('TestJenkins')
                    scanCredentialsId('GG3')
                    checkoutCredentialsId('GG3')
                  	buildOriginBranch(true)
                  	buildOriginBranchWithPR(false)
                  	buildOriginPRMerge(true)
                    buildOriginPRHead(false)
                    buildForkPRMerge(true)
                  	buildForkPRHead(false)
                  	apiUri('')
                    id(multibranchPipelineJobName)
                }
            }
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(20)
        }
    }
}

queue(multibranchPipelineJobName)

listView(multibranchPipelineJobName + '/' + 'Main branch') {
    description('Building main branch')
    recurse()
     jobs {
        regex(/master/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView(multibranchPipelineJobName + '/' + 'Working branches') {
    description('Building working branches and PRs')
    recurse()
     jobs {
        regex(/^PR-.*$/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}