FROM jenkins/jenkins:lts
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

USER root

RUN groupadd docker \
    && usermod -a -G docker jenkins

ARG DOCKER_CLIENT=docker-20.10.5.tgz

RUN cd /tmp/ \
    && curl -sSL -O https://download.docker.com/linux/static/stable/x86_64/${DOCKER_CLIENT} \
    && tar zxf ${DOCKER_CLIENT} \
    && mkdir -p /usr/local/bin \
    && mv ./docker/docker /usr/local/bin \
    && chmod +x /usr/local/bin/docker \
    && rm -rf /tmp/*

USER jenkins

RUN jenkins-plugin-cli --plugins "git workflow-aggregator pipeline-stage-view docker-plugin docker-workflow locale pipeline-graph-view ssh-agent"