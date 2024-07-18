#!/bin/bash

# Path to JSON files
original_json="original.json"
patch_json="patch.json"
patched_json="patched.json"

# Read original JSON
original=$(cat "$original_json")

# Apply patch using jq
#patched=$(jq --argjson patch "$(cat "$patch_json")" '. as $original | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value)' <<< "$original")
#patched=$(jq --argjson patch "$(cat "$patch_json")" '. as $original | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value) | . ' <<< "$original")

#patched=$(jq --argjson patch "$(cat "$patch_json")" '. as $original | reduce $patch[] as $p ({}; .[$p.path | ltrimstr("/")] = $p.value)' <<< "$original")

#jq --argjson patch "$(cat "$patch_json")" '
#    . as $original |
#    reduce $patch[] as $p (
#        $original;
#        .[$p.path | ltrimstr("/")] = $p.value
#    )
#' <<< "$original" > "$patched_json"

# Apply patch using jq and save patched JSON to file


# Apply patch using jq and save patched JSON to file
jq --argjson patch "$(cat "$patch_json")" '
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
