#!/bin/bash

GEN_DIR=gen
mkdir -p $GEN_DIR
rm -Rf $GEN_DIR/*

# Path to JSON files
ORIGINAL_JSON="ci-config-defaults.json"
PATCH_JSON="patches/patch.json"
PATCHED_ORIGINAL="$GEN_DIR/$ORIGINAL_JSON"

# Read ORIGINAL_JSON_CONTENT JSON
ORIGINAL_JSON_CONTENT=$(cat "$ORIGINAL_JSON")

# Define subcontent that should be added to the patch
BUILD_TASK='["maven", {"image": "myimage"}]'

# Export variable for envsubst
export BUILD_TASK

# Substitute variables in the patch template
envsubst < $PATCH_JSON > $GEN_DIR/gen-patch.json
cat $GEN_DIR/gen-patch.json


# Apply patch using jq and save patched JSON to file
jq --argjson patch "$(cat "$GEN_DIR/gen-patch.json")" '
    . as $original |
    reduce $patch[] as $p (
        $original;
        .[$p.path | sub("^/"; "")] = $p.value
    )
' <<< "$ORIGINAL_JSON_CONTENT" > "$PATCHED_ORIGINAL"

# Save patched JSON to file
#echo "$patched" >> "$patched_json"

# Print the patched JSON
cat "$PATCHED_ORIGINAL"
