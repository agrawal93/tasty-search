FROM java:8-jre-alpine
ARG VERSION=1.0.0
ADD target\tasty-search-$VERSION.jar /tasty-search.jar
CMD [ "java", "-jar", "/tasty-search.jar" ]
EXPOSE 8080