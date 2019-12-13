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
echo "Commit git"
echo "-----------------------------------------------------------"

echo "VERSION_CURRENT: ${VERSION_CURRENT}"
echo "VERSION_NEXT: ${VERSION_NEXT}"
echo "IDEA_VERSION_CURRENT: ${IDEA_VERSION_CURRENT}"
echo "VERSION_CURRENT_FULL: ${VERSION_CURRENT_FULL}"
echo "VERSION_NEXT_FULL: ${VERSION_NEXT_FULL}"

#
# Commit to git and push
#
# Params:
# 	- 0: version
function commit_git() {
  local version="$1"
  echo "Commit to git and push version ${version}"
  git commit -a -m "Promote to version ${version}"
  git push
}

commit_git "${VERSION_NEXT_FULL}"
