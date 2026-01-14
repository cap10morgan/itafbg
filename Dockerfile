FROM clojure:temurin-25-tools-deps-1.12.4.1582-trixie-slim AS build

WORKDIR /usr/src/itafbg

COPY deps.edn .
RUN clojure -P

COPY . .
RUN clojure -T:build uber

EXPOSE 80

CMD ["docker/run-at.sh"]
