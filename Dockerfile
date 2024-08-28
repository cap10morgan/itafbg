FROM clojure:temurin-21-tools-deps-1.11.4.1474-bookworm-slim AS build

WORKDIR /usr/src/itafbg

COPY deps.edn .
RUN clojure -P

COPY . .
RUN clojure -T:build uber

FROM eclipse-temurin:21-jre-noble AS run

WORKDIR /opt/itafbg

COPY --from=build /usr/src/itafbg/target/isthereafuckingbroncosgame.web.static.jar ./

EXPOSE 80

CMD ["java", "-jar", "isthereafuckingbroncosgame.web.static.jar"]
