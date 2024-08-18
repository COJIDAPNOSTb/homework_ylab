
FROM tomcat:10.1-jdk17


WORKDIR /usr/local/tomcat


COPY target/homework333-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/


EXPOSE 8080

# Стартуем Tomcat
CMD ["catalina.sh", "run"]
