@GrabResolver(name='maven-central', root='https://repo1.maven.org/maven2/')
@Grab(group='com.google.code.gson', module='gson', version='2.8.8')
@Grab(group='com.github.java-json-tools', module='json-patch', version='1.13')

import com.google.gson.JsonParser
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.github.fge.jsonpatch.JsonPatch
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.diff.JsonDiff

// Paths to the JSON files
def jsonFilePath = 'ci-config-defaults.json'
def patchFilePath = 'patches/patch.json'

// Read the JSON files
def jsonContent = new File(jsonFilePath).text
def patchContent = new File(patchFilePath).text

// Parse JSON using Gson
//JsonElement jsonElement = JsonParser.parseString(jsonContent)
//JsonObject jsonObject = jsonElement.getAsJsonObject()

// Convert the parsed JSON to Jackson JsonNode
def objectMapper = new ObjectMapper()
JsonNode patchNode = objectMapper.readTree(patchContent)
JsonNode jsonNode = objectMapper.readTree(jsonContent)

// Apply the JSON Patch
JsonPatch patch = JsonPatch.fromJson(patchNode)
JsonNode patchedNode = patch.apply(jsonNode)

// Convert the patched JsonNode back to JSON
def patchedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(patchedNode)

// Print the patched JSON
println(patchedJson)
