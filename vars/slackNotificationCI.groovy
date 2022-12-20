#!/usr/bin/env groovy

// CI Message

def call(String channel="jenkins-slack", String title="jenkins", String status="failure", Map body=[:], Map fields=[:]) {
    current_time = sh(script: "date", returnStdout: true).trim()

    start_color   = "#0DADEA" // blue
    failure_color = "#FF0000" // red
    success_color = "#18be52" // green
    
    if (status == "start") {
        
    }
    
    def attachments = [
        [
            "fallback": title,
            "color": "#FFFFFF",
            "blocks": [
                [
                    "type": "header",
                    "text": [
                        "type": "plain_text",
                        "text": title,
                        "emoji": ture
                    ]
                ],
                [
                    "type": "section",
                    "fields": [
                        slackMdFields("*TimeStampe:*\n${current_time}")
                    ]
                ]
            ]
        ]
    ]

    if(body.BRANCH) {
        attachments.getAt(0).blocks.getAt(1).fields.add(slackMdFields("text": "*Branch:*\n${body.BRANCH}"))
    }
    print()
}

def slackMdFields(String message="") {
    return [
        "type": "mrkdwn",
        "text": message
    ]
}