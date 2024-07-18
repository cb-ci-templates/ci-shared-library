#! /bin/bash
groovy patchGroovyJson.groovy
./patchJson.sh
./patchJsonEnvSubst.sh
./patchSimple.sh
./patchYaml.sh