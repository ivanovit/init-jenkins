import jenkins.model.*
import hudson.model.*
import hudson.security.*
import org.jenkinsci.plugins.*

import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

def env = System.getenv()
def github_user = env["GITHUB_USER"]
def github_private_key = env['GITHUB_PRIVATE_KEY']
def github_pass = env["GITHUB_PASS"]
def github_ssh_credentials_id = "github_ssh_credentials_id";
def github_standart_credentials_id = "github_standart_credentials_id";

if(github_user && github_private_key) {
    def github_ssh_credentials = new BasicSSHUserPrivateKey(
        CredentialsScope.GLOBAL,
        github_ssh_credentials_id,
        github_user,
        new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(github_private_key),
        null,
        "Private key for accessing github"
    )
    addOrUpdate(github_ssh_credentials);
    println "Registed private key for accessing github"
}

if(github_user && github_pass) {
    def github_standart_credentials = new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL,
        github_standart_credentials_id,
        "Credentials for accessing github api",
        github_user,
        github_pass
    )
    addOrUpdate(github_standart_credentials);
    println "Registed credentials for accessing github api"
}


def addOrUpdate(BaseStandardCredentials current, Domain domain=Domain.global()) {
    def idMatcher = CredentialsMatchers.withId(current.getId())
    def credentials_store = Jenkins.getInstance().getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
    def allCreds = credentials_store.getCredentials(domain)

    def existing_credentials = CredentialsMatchers.firstOrNull(allCreds, idMatcher)
    if(existing_credentials != null) {
        credentials_store.updateCredentials(domain, existing_credentials, current)
    } else {
        credentials_store.addCredentials(domain, current)
    }
}


