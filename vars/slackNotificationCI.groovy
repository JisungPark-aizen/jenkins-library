#!/usr/bin/env groovy

// CI Message

def call(String channel="jenkins-slack", String title="jenkins", String status="failure", Map fields=[:]) {
    current_time = sh(script: "date", returnStdout: true).trim()
    repository_name = env.GIT_URL.split('/').last()
    git_url_regex = ".*[^\\.git]"

    start_color   = "#0DADEA" // blue
    failure_color = "#FF0000" // red
    success_color = "#18be52" // green
    
    if (status == "start") {
        color = start_color
    } else if(status == "failure") {
        color = failure_color
    } else if(status == "success") {
        color = success_color
    } else {
        error("not status")
    }
    
    def attachments = [
        [
            "fallback": title,
            "color": color,
            "blocks": [
                [
                    "type": "header",
                    "text": [
                        "type": "plain_text",
                        "text": title,
                        "emoji": true
                    ]
                ],
                [
                    "type": "section",
                    "fields": [
                        [
                            "type": "mrkdwn",
                            "text": "*Branch:*\n${env.GIT_BRANCH}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Date:*\n${current_time}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Job URL:*\n<${env.RUN_DISPLAY_URL}|Jenkins Job>"
                        ]
                    ]
                ]
            ]
        ]
    ]
    if(env.GIT_URL.contains("codecommit")) {
        GIT_RUL = "https://ap-northeast-2.console.aws.amazon.com/codesuite/codecommit/repositories/${repository_name}/commit/${env.GIT_COMMIT}"
    } else {
        GIT_URL = env.GIT_URL.findAll(git_url_regex)[0]
        GIT_URL = "${GIT_URL}/commit/${env.GIT_COMMIT}"
    }
    attachments.getAt(0).blocks.getAt(1).fields.add(slackMdFields("*Commit ID:*\n<${GIT_URL}|${env.GIT_COMMIT}>"))

    if (!fields.isEmpty()) {
        fields.each { it ->
            attachments.getAt.blocks.getAt(1).fields.add(it)
        }
    }

    slackSend(channel: channel, attachments: attachments)
}

def slackMdFields(String message="") {
    return [
        "type": "mrkdwn",
        "text": message
    ]
}