#!/usr/bin/env groovy

def call() {
    echo "build"
    sh "env"
    echo "${JENKINS_HOME}"
    echo "${env.JENKINS_HOME}"
    
    if(env.GIT_URL.contains("com")) {
        echo "${env.GIT_URL}"
    }
}