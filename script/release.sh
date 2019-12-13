#!/usr/bin/env bash

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

echo "==========================================================="
echo "Release"
echo "==========================================================="

echo "VERSION_CURRENT: ${VERSION_CURRENT}"
echo "VERSION_NEXT: ${VERSION_NEXT}"
echo "IDEA_VERSION_CURRENT: ${IDEA_VERSION_CURRENT}"
echo "VERSION_CURRENT_FULL: ${VERSION_CURRENT_FULL}"
echo "VERSION_NEXT_FULL: ${VERSION_NEXT_FULL}"
echo "CURRENT_BRANCH: $(git branch | grep '^*')"

if git rev-parse "${VERSION_NEXT_FULL}" >/dev/null 2>&1; then
  echo "Tag ${VERSION_NEXT_FULL} exists, cannot proceed"
  exit 1
else
  echo "Tag ${VERSION_NEXT_FULL} doesn't exist"
fi

read -p "Continue with those versions (y/n)? " choice
case "${choice}" in
y | Y) ;;
n | N) exit 1 ;;
*)
  echo "invalid choice"
  exit 1
  ;;
esac

#
# Releases this version by calling script/update_version, then commit to git,
# then calling script/build_plugin, then manualy add to github.
#
# Params:
# 	- 0: current version
# 	- 1: next version
# 	- 2: current idea version
# 	- 3: current version in full (version_current+idea_version)
# 	- 4: next version in full (version_next+idea_version)
function release() {
  local version_current="$1"
  local version_next="$2"
  local idea_version_current="$3"
  local version_current_full="$4"
  local version_next_full="$5"

  # Update versions in all files
  ./script/update_versions.sh "${version_current}" "${version_next}" "${idea_version_current}"
  if [ $? -gt 0 ]; then
    echo Script failed: ./script/update_versions.sh "${version_current}" "${version_next}" "${idea_version_current}"
    exit 1
  fi

  # Commit to git and push
  ./script/commit_git.sh "${version_current}" "${version_next}" "${idea_version_current}"
  if [ $? -gt 0 ]; then
    echo Script failed: ./script/commit_git.sh "${version_current}" "${version_next}" "${idea_version_current}"
    exit 1
  fi

  # Build and package plugin in jar
  ./script/build_plugin.sh "${version_current}" "${version_next}" "${idea_version_current}"
  if [ $? -gt 0 ]; then
    echo Script failed: ./script/build_plugin.sh "${version_current}" "${version_next}" "${idea_version_current}"
    exit 1
  fi

  # Push tag and manual finish (in github)
  ./script/push_tag.sh "${version_current}" "${version_next}" "${idea_version_current}"
  if [ $? -gt 0 ]; then
    echo Script failed: ./script/push_tag.sh "${version_current}" "${version_next}" "${idea_version_current}"
    exit 1
  fi
}

release "${VERSION_CURRENT}" "${VERSION_NEXT}" "${IDEA_VERSION_CURRENT}" "${VERSION_CURRENT_FULL}" "${VERSION_NEXT_FULL}"
