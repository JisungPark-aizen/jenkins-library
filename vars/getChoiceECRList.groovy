#!/usr/bin/env groovy

def call(String ecr_credit_name="", String ecr_repo_name="", String include_tag="", String exclude_tag="", int limit=10) {
    ecr_script = """
import jenkins.model.*

def jenkinsCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        null,
        null
);
for (creds in jenkinsCredentials) {
    if(creds.id == \"${ecr_credit_name}\"){
        AWS_ACCESS_KEY_ID = creds.getAccessKey()
        AWS_SECRET_ACCESS_KEY = creds.getSecretKey()
    }
}

def aws_command = \"\"\"
env AWS_ACCESS_KEY_ID=\${AWS_ACCESS_KEY_ID}
env AWS_SECRET_ACCESS_KEY=\${AWS_SECRET_ACCESS_KEY}
aws ecr describe-images \
--region ap-northeast-2 \
--repository-name ${ecr_repo_name} \
--output text \
--query imageDetails[*].imageTags[*]
\"\"\"

def sort_command = 'sort -rV
def proc = aws_command.execute() | sort_command.execute()

if(${include_tag} != "") {
    def include_command = \"grep -E \${tag_filter}\"
    proc = proc | include_command.execute()
}
if(${exclude_tag} != "") {
    def exclude_command = \"grep -vE \${tag_filter}\"
    proc = proc | exclude_command.execute()
}

def limit_command = \"head -n \${limit}\"
proc = proc | limit_command.execute()

proc.waitFor() 

def result = proc.text.tokenize()
return result
"""

    return getChoiceTemplate("ChoiceParameter", "ecr_image", "Select the deploy image from the Dropdown List", script)
}