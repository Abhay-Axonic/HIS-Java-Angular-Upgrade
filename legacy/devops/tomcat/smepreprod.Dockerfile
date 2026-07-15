FROM tomcat:9.0.97-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*
RUN rm -rf /usr/local/tomcat/conf/server.xml
COPY ./devops/tomcat/server.xml  /usr/local/tomcat/conf 
COPY ./HIS-WAR/target/HIS-SME.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
