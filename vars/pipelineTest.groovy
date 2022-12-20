#!/usr/bin/env groovy

def call() {
    echo "build"
    sh "env"
    echo "${JENKINS_HOME}"
    echo "${env.JENKINS_HOME}"
    
    if(env.GIT_URL.contains("github")) {
        echo "success"
        GIT_URL="test"
    } else {
        GIT_URL=env.GIT_URL
    }
    echo "${env.GIT_URL}"
    echo "${GIT_URL}"
}