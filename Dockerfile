FROM clojure:temurin-21-tools-deps-1.11.4.1474-bookworm-slim AS build

WORKDIR /usr/src/itafbg

COPY deps.edn .
RUN clojure -P

COPY . .
RUN clojure -T:build uber

EXPOSE 80

CMD ["docker/run-at.sh"]
