#!/bin/bash
#https://jsonpatch.com/
set -x

# Check if yq and jq are installed
if ! command -v yq &> /dev/null; then
    echo "yq could not be found. Please install yq."
    exit 1
fi

if ! command -v jq &> /dev/null; then
    echo "jq could not be found. Please install jq."
    exit 1
fi

# Path to JSON files
GEN_DIR=gen
mkdir -p $GEN_DIR
rm -Rf $GEN_DIR/*

# Path to JSON files
ORIGINAL_YAML="ci-config-defaults.yaml"
ORIGINAL_JSON="$GEN_DIR/ci-config-defaults.json"
PATCH_JSON="patches/patch.json"
PATCHED_ORIGINAL_JSON="$GEN_DIR/ci-config-defaults.json"
PATCHED_ORIGINAL_YAML="$GEN_DIR/$ORIGINAL_YAML"

#Use Jsonpatch
# Convert YAML to JSON
yq -o=json e '.' $ORIGINAL_YAML > $ORIGINAL_JSON
cat $ORIGINAL_JSON

# Read ORIGINAL_JSON_CONTENT JSON
ORIGINAL_JSON_CONTENT=$(cat "$ORIGINAL_JSON")
# Apply patch using jq and save patched JSON to file
jq --argjson patch "$(cat "$PATCH_JSON")" '
    . as $ORIGINAL_JSON_CONTENT |
    reduce $patch[] as $p (
        $ORIGINAL_JSON_CONTENT;
        .[$p.path | sub("^/"; "")] = $p.value
    )
' <<< "$ORIGINAL_JSON_CONTENT" > "$PATCHED_ORIGINAL_JSON"

# Convert JSON back to YAML
yq -P -o=yaml . $PATCHED_ORIGINAL_JSON > $PATCHED_ORIGINAL_YAML

echo "Output the patched JSON content"
cat $PATCHED_ORIGINAL_JSON

echo "Output the patched YAML content"
cat $PATCHED_ORIGINAL_YAML
