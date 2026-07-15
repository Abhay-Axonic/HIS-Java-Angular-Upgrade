#FROM tomcat:8.0.51-jre8-alpine
FROM tomcat:8.5.75-jre8-openjdk-bullseye
RUN rm -rf /usr/local/tomcat/webapps/*
RUN rm -rf /usr/local/tomcat/conf/server.xml
COPY ./devops/tomcat/server.xml  /usr/local/tomcat/conf 
COPY ./HIS-WAR/target/HIS-WAR.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
