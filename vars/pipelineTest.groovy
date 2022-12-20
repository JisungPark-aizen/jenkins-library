#!/usr/bin/env groovy

def call() {
    echo "build"
    sh "env"
    echo "${JENKINS_HOME}"
    echo "${env.JENKINS_HOME}"
    
    echo "${env.GIT_URL}"

    git_url = env.GIT_URL.findAll(".*[^\\.git]")[0]
    echo "${git_url}"
}