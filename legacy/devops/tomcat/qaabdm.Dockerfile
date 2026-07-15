# Use a Tomcat 9 image with JDK 17
FROM tomcat:9.0.97-jdk17-temurin

# Set Java heap size
#ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Set Java options for memory allocation and New Relic agent
ENV JAVA_OPTS="-Xmx4000m -Xms2000m -javaagent:/usr/local/tomcat/newrelic/newrelic.jar"

# Remove default web applications and configuration files
    RUN rm -rf /usr/local/tomcat/webapps/* \
    && rm -rf /usr/local/tomcat/conf/server.xml \
    && rm -rf /usr/local/tomcat/conf/context.xml

# Add custom server and context configuration files
COPY ./devops/tomcat/server.xml /usr/local/tomcat/conf
COPY ./devops/tomcat/context.xml /usr/local/tomcat/conf

# Add the application WAR file and deploy it as ROOT
COPY ./HIS-WAR/target/HIS-WAR.war /usr/local/tomcat/webapps/ROOT.war

# Add New Relic monitoring agent and configuration
RUN mkdir -p /usr/local/tomcat/newrelic
COPY ./HIS-WAR/src/main/webapp/WEB-INF/lib/newrelic.jar /usr/local/tomcat/newrelic/newrelic.jar
COPY ./HIS-WAR/src/main/webapp/WEB-INF/lib/newrelic.yml /usr/local/tomcat/newrelic/newrelic.yml

# Create and copy ServerInfo.properties to hide Tomcat version  

RUN mkdir -p /usr/local/tomcat/lib/org/apache/catalina/util
COPY ./devops/tomcat/ServerInfo.properties /usr/local/tomcat/lib/org/apache/catalina/util/ServerInfo.properties


# Set the default command to run Tomcat
CMD ["catalina.sh", "run"]
