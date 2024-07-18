#!/bin/bash
#https://jsonpatch.com/
set -x
#This script doesnt work yet, need to be fixed

YAML_FILE=$1
PATCH_JSON=patch.json
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
#yq  '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE
#yq  '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE > patche_yq.yaml
#yq -i '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE

#Use jq
#jq '.build.maven.image = "maven:3-amazoncorretto-17"' $YAML_FILE

#Use Jsonpatch
# Convert YAML to JSON
yq -o=json e '.' $YAML_FILE > original.json

# Apply the JSON patch
jq --argjson patch "$(cat "$PATCH_JSON")" '
    . as $original |
    reduce $patch[] as $p (
        $original;
        .[$p.path | sub("^/"; "")] = $p.value
    )
' <<< "$original" >  patched.json


# Convert JSON back to YAML
yq -o=yaml e '.' patched.json > patched.yaml
#yq -o=yaml e '.' patched.json

# Output the patched YAML content
cat patched.yaml
