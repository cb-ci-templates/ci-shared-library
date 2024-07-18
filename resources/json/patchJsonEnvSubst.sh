#!/bin/bash

# Path to JSON files
original_json="original.json"
patch_json="patchEnvSubst.json"
patched_json="patched.json"
# Read original JSON
original=$(cat "$original_json")


# Define your variable
BUILD_TASK='["maven", {"image": "myimage"}]'

# Export variable for envsubst
export BUILD_TASK

# Substitute variables in the patch template
envsubst < $patch_json > gen-patch.json
cat gen-patch.json


# Apply patch using jq and save patched JSON to file
jq --argjson patch "$(cat "gen-patch.json")" '
    . as $original |
    reduce $patch[] as $p (
        $original;
        .[$p.path | sub("^/"; "")] = $p.value
    )
' <<< "$original" > "$patched_json"

# Save patched JSON to file
#echo "$patched" >> "$patched_json"

# Print the patched JSON
cat "$patched_json"
