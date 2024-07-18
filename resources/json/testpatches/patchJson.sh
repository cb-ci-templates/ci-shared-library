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

# Apply patch using jq and save patched JSON to file
jq --argjson patch "$(cat "$PATCH_JSON")" '
    . as $ORIGINAL_JSON_CONTENT |
    reduce $patch[] as $p (
        $ORIGINAL_JSON_CONTENT;
        .[$p.path | sub("^/"; "")] = $p.value
    )
' <<< "$ORIGINAL_JSON_CONTENT" > "$PATCHED_ORIGINAL"

# Save patched JSON to file
#echo "$patched" >> "$PATCHED_ORIGINAL"

# Print the patched JSON
cat "$PATCHED_ORIGINAL"

#patched=$(jq --argjson patch "$(cat "$PATCH_JSON")" '. as $ORIGINAL_JSON_CONTENT | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value)' <<< "$ORIGINAL_JSON_CONTENT")
#patched=$(jq --argjson patch "$(cat "$PATCH_JSON")" '. as $ORIGINAL_JSON_CONTENT | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value) | . ' <<< "$ORIGINAL_JSON_CONTENT")
#patched=$(jq --argjson patch "$(cat "$PATCH_JSON")" '. as $ORIGINAL_JSON_CONTENT | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value)' <<< "$ORIGINAL_JSON_CONTENT")

#jq --argjson patch "$(cat "$PATCH_JSON")" '
#    . as $ORIGINAL_JSON_CONTENT |
#    reduce $patch[] as $p (
#        $ORIGINAL_JSON_CONTENT;
#        .[$p.path | ltrimstr("/")] = $p.value
#    )
#' <<< "$ORIGINAL_JSON_CONTENT" > "$PATCHED_ORIGINAL"





