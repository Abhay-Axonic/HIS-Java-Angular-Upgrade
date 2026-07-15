FROM tomcat:8.0.51-jre8-alpine

RUN rm -rf /usr/local/tomcat/webapps/*
RUN rm -rf /usr/local/tomcat/conf/server.xml
COPY ./devops/tomcat/server.xml  /usr/local/tomcat/conf 
COPY ./HIS-WAR/target/HIS-ATHAAYU.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
