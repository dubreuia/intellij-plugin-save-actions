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
echo "Update version"
echo "-----------------------------------------------------------"

echo "VERSION_CURRENT: ${VERSION_CURRENT}"
echo "VERSION_NEXT: ${VERSION_NEXT}"
echo "IDEA_VERSION_CURRENT: ${IDEA_VERSION_CURRENT}"
echo "VERSION_CURRENT_FULL: ${VERSION_CURRENT_FULL}"
echo "VERSION_NEXT_FULL: ${VERSION_NEXT_FULL}"

#
# Changes the version from source to target in the proper files (build, plugin, docs).
#
# Params:
# 	- 0: source version
# 	- 1: target version
function change_version() {
  local source="$1"
  local target="$2"

  echo "Updating ${source} -> ${target}"

  for file in "build.gradle" \
    "docs/badge-jetbrains-website.svg" \
    "src/main/resources/META-INF/plugin.xml"; do
    sed "s/${source}/${target}/g" ${file} >${file}.next && mv ${file}.next ${file}
  done
}

change_version "${VERSION_CURRENT_FULL}" "${VERSION_NEXT_FULL}"
echo "The tag for this branch is ${VERSION_NEXT_FULL}"
