import jenkins.model.Jenkins
import hudson.model.FreeStyleProject
import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.SubmoduleConfig
import hudson.plugins.git.extensions.impl.PathRestriction
import hudson.model.Cause
import hudson.model.Cause.UserIdCause

import javaposse.jobdsl.plugin.ExecuteDslScripts
import javaposse.jobdsl.plugin.RemovedJobAction

import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

def env = System.getenv()
seed_jobs_repo = env['SEED_JOBS_REPO']

if(seed_jobs_repo) {
  if(!Jenkins.instance.getItemMap().containsKey("seed-job")) {
    def seedJob = Jenkins.instance.createProject(FreeStyleProject.class, "seed-job")
    def idMatcher = CredentialsMatchers.withId("github_standart_credentials_id");

    available_credentials = CredentialsProvider.lookupCredentials(StandardUsernameCredentials.class)

    existing_credentials =
      CredentialsMatchers.firstOrNull(
        available_credentials,
        idMatcher
      )

    def userRemoteConfig = new UserRemoteConfig(seed_jobs_repo, null, null, existing_credentials.id)

    def scm = new GitSCM(
      Collections.singletonList(userRemoteConfig),
      Collections.singletonList(new BranchSpec("master")),
      false,
      Collections.<SubmoduleConfig>emptyList(),
      null,
      null,
      null)

    scm.getExtensions().add(new PathRestriction("jobs/**/*.*",""))
    seedJob.scm = scm

    def scriptLocation = new ExecuteDslScripts.ScriptLocation("false", "jobs/**/*.groovy", null)
    seedJob.buildersList.add(new ExecuteDslScripts(scriptLocation, false, RemovedJobAction.DELETE))

    seedJob.save()
    seedJob.scheduleBuild(new Cause.UserIdCause())
  }
}