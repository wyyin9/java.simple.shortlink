FROM java:8
VOLUME /tmp
COPY target/shortlink-app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]