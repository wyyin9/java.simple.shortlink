FROM java:8
VOLUME /tmp
COPY springboot-shortlink-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]