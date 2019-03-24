#!/usr/bin/env bash

function build_plugin {
	local version_current="$( grep "^version" build.gradle |
			  grep -oP "version \K[\w\.']+" |
			  sed "s/'//g" )"
	local version_idea="$( grep "^    version" build.gradle |
			       grep -oP "version \K[\w\.']+" |
			       sed "s/'//g" )"
	local version="${version_current}+${version_idea}"

	echo "Building plugin version ${version}"

	./gradlew clean
	./gradlew buildPlugin
	cp build/libs/intellij-plugin-save-actions-*.jar "intellij-plugin-save-actions-${version}.jar"
}

build_plugin

