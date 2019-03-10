FROM openjdk:8-alpine
MAINTAINER aviatorsbot.com

WORKDIR /app
COPY target/universal/aviatorsbot-scala-1.0.zip /app/app.zip

RUN /bin/sh -c "apk add --no-cache bash"
RUN unzip /app/app.zip
RUN ls /app
RUN chmod -R a+x /app/bin

EXPOSE 80

CMD ["/app/bin/aviatorsbot-scala"]