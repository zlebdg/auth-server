FROM alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk --update add ttf-dejavu openjdk8-jre bash \
    && rm -rf /var/cache/apk/*

EXPOSE 8080

COPY target/*.jar /app.jar

CMD java -jar app.jar
