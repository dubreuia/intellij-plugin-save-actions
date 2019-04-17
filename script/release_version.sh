#!/usr/bin/env bash

VERSION_NEXT=${1?"Usage: $0 VERSION_NEXT"}
VERSION_CURRENT="$( grep "^version" build.gradle |
		grep -oP "version \K[\w\.']+" |
		sed "s/'//g" )"
IDEA_VERSION_CURRENT="$( grep "^    version" build.gradle |
		     grep -oP "version \K[\w\.']+" |
		     sed "s/'//g" )"
VERSION="${VERSION_NEXT}+${IDEA_VERSION_CURRENT}"

#
# Changes the version from source to target in the proper files (build, plugin, docs).
#
# Params:
# 	- 0 : source version
# 	- 1 : target version
function change_version {
	local source="$1"
	local target="$2"

	echo "Updating ${source} -> ${target}"

	for file in "build.gradle" \
		"docs/badge-jetbrains-website.svg" \
		"src/main/resources/META-INF/plugin.xml"
	do
		sed "s/${source}/${target}/g" ${file} > ${file}.next && mv ${file}.next ${file}
	done
}

change_version "${VERSION_CURRENT}" "${VERSION}"
echo "The tag for this branch is ${VERSION}"

