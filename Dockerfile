FROM clojure:openjdk-11-tools-deps-1.11.1.1113-slim-bullseye AS build

WORKDIR /usr/src/itafbg

COPY deps.edn .
RUN clojure -P

COPY . .
RUN clojure -T:build uber

FROM eclipse-temurin:17-jre-focal AS run

WORKDIR /opt/itafbg

COPY --from=build /usr/src/itafbg/target/isthereafuckingbroncosgame.web.jar ./

EXPOSE 80

CMD ["java", "-jar", "isthereafuckingbroncosgame.web.jar"]
