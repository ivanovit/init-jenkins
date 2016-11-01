FROM jenkins
#COPY init.config /usr/share/jenkins/ref/init.groovy.d/
#RUN /usr/local/bin/install-plugins.sh docker-slaves github-branch-source:1.8
COPY plugins.txt /usr/share/jenkins/ref/
COPY init.config /usr/share/jenkins/ref/init.groovy.d/
#RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh docker-slaves github-branch-source:1.8