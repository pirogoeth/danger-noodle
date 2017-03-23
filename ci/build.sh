#!/usr/bin/env bash

set -e

source ci/common.sh

########
# CLEAN
########

wprint "Cleaning up build directory..."
if [[ -d "build" ]] ; then
    cleanFiles=$(find build -name '*.class')
    if [[ ! -z "${cleanFiles}" ]] ; then
        echo ${cleanFiles} | xargs rm
    fi
fi
wprint "Cleaned up build directory" "okay"


########
# BUILD
########

wprint "Building danger-noodle to 'build/'..."

[[ ! -d "build" ]] && mkdir build
sources=$(find src -name '*.java')
javac -g -d build -Werror -Xlint:unchecked -parameters ${sources}

[[ $? != 0 ]] && exit 1

wprint "Built danger-noodle to 'build/'..." "okay"


########
# RESOURCES
########

wprint "Copying resources to 'build/'..."

if [[ -d "src/resources" ]] ; then
    [[ -d "build/resources" ]] && rm -rf build/resources
    cp -fra src/resources build/resources
fi

wprint "Copied resources to 'build/resources'!" "okay"


########
# PACKAGE
########

wprint "Packing danger-noodle into jarfile..."

jar cf danger-noodle.jar -C build .

[[ $? != 0 ]] && exit 1

wprint "Packed danger-noodle to './danger-noodle.jar'!" "okay"


########
# DONE
########

wprint "danger-noodle built successfully!" "done!"
