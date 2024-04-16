#!/bin/bash
# Copyright 2021 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#!/bin/bash
# For this example we will just create a access token using gcloud substitute with your own approach
token=$(gcloud auth print-access-token)
# base64 encode the username and password
docker_token=$(echo -n "gclouddockertoken:$token" | base64 | tr -d "\n")
#Create Docker config.json with the credentials
cat > config.json <<- EndOfMessage
{
  "auths": {
    "gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    },
    "us.gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    },
    "eu.gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    },
    "asia.gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    },
    "staging-k8s.gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    },
    "marketplace.gcr.io": {
      "auth": "$docker_token",
      "email": "not@val.id"
    }
  }
}
EndOfMessage