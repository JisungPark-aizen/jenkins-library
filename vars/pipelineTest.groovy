#!/usr/bin/env groovy

def call() {
    stage("lib") {
        echo "lib"
    }
    stage("build") {
        echo "build"
        sh "env"
        echo "${JENKINS_HOME}"
        echo "${env.JENKINS_HOME}"
    }
}