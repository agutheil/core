http://spring.io/guides/gs/consuming-rest/

curl 'http://localhost:8080/api/articles/1' -H 'Authorization: Bearer 2c7489df-ed7c-4431-9384-b5cf369f11ca' -H 'Accept: application/json, text/plain, */*' -H 'Referer: http://localhost:8080/'

curl 'http://localhost:8080/oauth/token' -H 'Authorization: Basic bWlnaHR5bWVyY2VhcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==' -H 'Accept: application/json' --data 'username=admin&password=admin&grant_type=password&scope=read&client_secret=mySecretOAuthSecret&client_id=mightymerceapp' --compressed