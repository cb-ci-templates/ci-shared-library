This repo contains sample Jenkins Pipelines, Shared Libraries and Pipeline Template catalogs.

# Docker build

To build a custom tool container:  (f.e. `resource/dockerfiles/Dockerfile-curl-jq-yq-git` )
* Macos requires buildx
```
cd resource/dockerfiles 
docker  buildx  build   -t caternberg/curl-yq-jq-git  .
docker push caternberg/curl-yq-jq
docker push caternberg/curl-yq-jq:<TAG>
```

 
