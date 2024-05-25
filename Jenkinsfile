#!groovy

pipeline {
    agent any
    options {
        ansiColor('xterm')
        timeout(time: 1, unit: 'HOURS')
    }

    tools {
        jdk 'java'
        gradle 'Gradle8.7'
    }

    environment {
        JOB_TIME = sh(returnStdout: true, script: "date '+%A %W %Y %X'").trim()
    }

    stages {
        stage('Checkout Source') {
            steps {
                sh 'echo $JOB_TIME'
                echo "--------------------------env--------------------------------"
                sh "printenv"
                echo "--------------------------env--------------------------------"
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : scm.branches,
                        doGenerateSubmoduleConfigurations: true,
                        extensions                       : scm.extensions + [[$class: 'SubmoduleOption', parentCredentials: true]],
                        userRemoteConfigs                : scm.userRemoteConfigs
                ])
                // get latest git message
                script {
                    env.GIT_COMMIT_MSG = sh(script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                }
            }
        }

        stage('Build') {
            steps {
                withGradle {
                    sh 'gradle clean bootJar -Pprofile=local --refresh-dependencies -x check --stacktrace --console=plain'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''ssh -tt $ssh_server << remotessh
                    cd $ssh_dir
                    sh ./stop.sh
                    exit
                    remotessh'''
                sh 'scp /opt/jenkins/data/jobs/Catch-Cash/workspace/build/libs/web-server.jar $ssh_server:$ssh_dir/web-server.jar'
                sh '''ssh -tt $ssh_server << remotessh
                    cd $ssh_dir
                    sh ./run.sh
                    exit
                    remotessh'''
            }
        }
    }

    post {
        always {
            script {
                BUILD_USER = currentBuild.rawBuild.getCause(Cause.UserIdCause)?.getUserId()
                if (!BUILD_USER) BUILD_USER = "Job Scheduler"
                COLOR_MAP = ['SUCCESS': 'good', 'FAILURE': 'danger',]
            }
            slackSend channel: '#ci-jenkins',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"
        }

        success {
            echo "Only when we haven't failed running the first stage"
        }

        failure {
            echo "Only when we fail running the first stage."
        }

        fixed {
            echo "Only when we fixed the previous failed stage."
        }
    }

# ssh setting
RUN echo "$ssh_usr:$ssh_pwd" >> ~/passwdfile && \
    chpasswd -c SHA512 < ~/passwdfile && \
    rm ~/passwdfile && \
    sed -i "s/#Port.*/Port 22/" /etc/ssh/sshd_config && \
    sed -i "s/#PermitRootLogin.*/PermitRootLogin yes/" /etc/ssh/sshd_config && \
    sed -i "s/#PasswordAuthentication.*/PasswordAuthentication yes/" /etc/ssh/sshd_config

# expose the port 22(which is the default port of ssh)
EXPOSE 22

# set entrypoint to restart ssh automatically
ENTRYPOINT service ssh restart && bash
}
