#!/bin/bash
#https://jsonpatch.com/
set -x


YAML_FILE=ci-config-defaults.yaml
PATCH_JSON=patches/patch.json
# Check if yq and jq are installed
if ! command -v yq &> /dev/null; then
    echo "yq could not be found. Please install yq."
    exit 1
fi

if ! command -v jq &> /dev/null; then
    echo "jq could not be found. Please install jq."
    exit 1
fi

#Use yq
yq  '.build.maven.image = "maven:3-amazoncorretto-test-simple"' $YAML_FILE
#yq  '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE > patche_yq.yaml
#yq -i '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE

#Use jq
#jq '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE