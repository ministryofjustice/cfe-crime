FROM amazoncorretto:17-alpine
RUN mkdir -p /opt/cfe-crime/
WORKDIR /opt/cfe-crime/
COPY ./build/libs/cfe-crime.jar /opt/cfe-crime/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8080 8096
ENTRYPOINT ["java","-jar","app.jar"]
