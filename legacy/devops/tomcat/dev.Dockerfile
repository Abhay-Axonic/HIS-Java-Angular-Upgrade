FROM tomcat:8.0.51-jre8-alpine
RUN apk add --no-cache logrotate
COPY ./devops/tomcat/logrotate /etc/logrotate.d/tomcat
RUN crond
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./HIS-WAR/target/HIS-WAR.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]