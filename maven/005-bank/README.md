# JIB
- mvn compile jib:dockerBuild
- docker image push docker.io/jmodevel/application.name:version
- If this hangs, 
  - Execute mvn compile jib:build
  - Pull image from Docker / Images / Hub repositories

# Docker compose
- docker-compose up -d in the folder where you have the docker-compose.yml file of the environment you want to run
- docker-compose down to stop the environment and remove the containers