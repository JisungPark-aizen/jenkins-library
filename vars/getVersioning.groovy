#!/usr/bin/env groovy

def call(Map git_info = [:]) {
    changelog_file = 'CHANGELOG.md'
    if (fileExists(changelog_file)) {
        sh "rm ${changelog_file}"
    }

    echo "${git_info.version_branch}" 
    echo "${git_info.credentials}"
    echo "https://${git_info.url}"
    // Version 관리 Git clone
    dir("version") {
        git branch: git_info.version_branch,
        credentialsId: git_info.credentials,
        url: "https://${git_info.url}"

        if (fileExists('CHANGELOG.md')) {
            // 이전 변경 이력에 이어서 이력을 남기기 위해 저장된 CHANGELOG파일 이동
            sh "mv CHANGELOG.md .."
        }
    }

    // standard-version을 사용 시 자동 Commit추가를 제외하기 위한 옵션 사용
    sh "standard-version --skip.commit=true --packageFiles 'manifest.json'"
    sh "mv CHANGELOG.md version/"

    // standard-version으로 생성된 현재 버전 가져오기
    release_version = get_git_first_tagname()

    // Version관리 repo에 push
    dir("version") {
        sh "git add CHANGELOG.md"
        sh "git commit -m ${release_version}"
        withCredentials([gitUsernamePassword(credentialsId: git_info.credentials, gitToolName: 'git-tool')]) {
            sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${git_info.url} HEAD:${git_info.version_branch}"
        }
    }

    withCredentials([gitUsernamePassword(credentialsId: env.GIT_CREDIT_NAME, gitToolName: 'git-tool')]) {
        sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${git_info.url} HEAD:${env.Branch} --tags"
    }

    return release_version
}

def get_git_first_tagname() {
    result = sh(script: "git tag --list --sort=-v:refname | cat | head -n 1", returnStdout: true).trim()
    return result
}