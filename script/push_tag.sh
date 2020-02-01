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
echo "Push tag"
echo "-----------------------------------------------------------"

echo "VERSION_CURRENT: ${VERSION_CURRENT}"
echo "VERSION_NEXT: ${VERSION_NEXT}"
echo "IDEA_VERSION_CURRENT: ${IDEA_VERSION_CURRENT}"
echo "VERSION_CURRENT_FULL: ${VERSION_CURRENT_FULL}"
echo "VERSION_NEXT_FULL: ${VERSION_NEXT_FULL}"

#
# Push tag and diff
#
# Params:
# 	- 0: version
function push_tag() {
  local version="$1"
  local git_tag="v$1"
  local git_tag_escape="${git_tag/+/%2B}"

  # Push tag and manual finish (in github)
  git push origin HEAD:"refs/tags/${git_tag}"
  echo "Version URL (github): https://github.com/dubreuia/intellij-plugin-save-actions/releases/new?tag=${git_tag_escape}"
  echo "Release title: Release ${version}"
  echo "File jar: $(ls "intellij-plugin-save-actions-${version}.jar")"
}

push_tag "${VERSION_NEXT_FULL}"
