FROM alpine

COPY target/*.jar /app.jar

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk --update add openjdk8-jre bash \
    && rm -rf /var/cache/apk/*

EXPOSE 8080

CMD java -jar app.jar
