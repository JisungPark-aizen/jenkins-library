#!/usr/bin/env groovy

def call() {
    echo "build"
    sh "env"
    echo "${JENKINS_HOME}"
    echo "${env.JENKINS_HOME}"
    
    if(env.GIT_URL.contains("github")) {
        echo "success"
        echo "${env.GIT_URL}"
    } else {
        echo "${env.GIT_URL}"
    }
}