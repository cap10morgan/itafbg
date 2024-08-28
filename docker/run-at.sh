#!/bin/bash

# This script exists because I couldn't get cron to do _anything_ in a Docker
# container.
# Stolen from here: https://gist.github.com/mowings/a32fbf4dd46d9fb9661822056ceda91c

build_site() {
  clojure -T:build dhtml && cat public/dynamic/index.html
}

# run once on startup
build_site

WAKEUP=$1

while :
do
  SECS=$(( $(date -d "today $WAKEUP" +%s) - $(date -d "now" +%s) ))
  if [[ $SECS -lt 0 ]]; then
    SECS=$(( $(date -d "tomorrow $WAKEUP" +%s) - $(date -d "now" +%s) ))
  fi
  echo "$(date +"%Y-%m-%d %T")| Will sleep for $SECS seconds."
  sleep $SECS & # sleep in the background to make script interruptible
  wait $!
  echo "$(date +"%Y-%m-%d %T")| Waking up"
  build_site
done
