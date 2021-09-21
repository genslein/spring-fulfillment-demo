#!/bin/zsh

while true
do
  curl -X 'POST' \
    'http://localhost:8080/customers' \
    -H 'accept: application/json'

  curl -X 'GET' \
    'http://localhost:8080/customers' \
    -H 'accept: application/json'

  curl -X 'GET' \
    'http://localhost:8080/actuator/health' \
    -H 'accept: */*'

  sleep 5
done
