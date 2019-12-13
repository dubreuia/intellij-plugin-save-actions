#!/usr/bin/env bash

set -e

VERSION_CURRENT=${1?"Usage: $0 VERSION_CURRENT VERSION_NEXT [IDEA_VERSION_CURRENT]"}
VERSION_NEXT=${2?"Usage: $0 VERSION_CURRENT VERSION_NEXT [IDEA_VERSION_CURRENT]"}
IDEA_VERSION_CURRENT=${3}

if [[ -z ${IDEA_VERSION_CURRENT} ]]; then
  VERSION_CURRENT_FULL="${VERSION_CURRENT}"
  VERSION_NEXT_FULL="${VERSION_NEXT}"
else
  VERSION_CURRENT_FULL="${VERSION_CURRENT}+${IDEA_VERSION_CURRENT}"
  VERSION_NEXT_FULL="${VERSION_NEXT}+${IDEA_VERSION_CURRENT}"
fi

echo "-----------------------------------------------------------"
echo "Build plugin"
echo "-----------------------------------------------------------"

echo "VERSION_CURRENT: ${VERSION_CURRENT}"
echo "VERSION_NEXT: ${VERSION_NEXT}"
echo "IDEA_VERSION_CURRENT: ${IDEA_VERSION_CURRENT}"
echo "VERSION_CURRENT_FULL: ${VERSION_CURRENT_FULL}"
echo "VERSION_NEXT_FULL: ${VERSION_NEXT_FULL}"

#
# Builds the plugin and creates the jar, then copies it with proper naming in root folder.
#
# Params:
# 	- 0: version
function build_plugin() {
  local version="$1"

  echo "Building plugin version ${version}"

  ./gradlew clean
  ./gradlew buildPlugin
  cp build/libs/intellij-plugin-save-actions-*.jar "intellij-plugin-save-actions-${version}.jar"
}

build_plugin "${VERSION_NEXT_FULL}"
