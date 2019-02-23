FROM openjdk:8-jre-stretch
MAINTAINER aviatorsbot.com

WORKDIR /app
COPY target/universal/aviatorsbot-scala-1.0.zip /app/app.zip

RUN unzip /app/app.zip
RUN ls /app
RUN chmod -R a+x /app/bin

ENTRYPOINT ["/app/bin/aviatorsbot-scala"]