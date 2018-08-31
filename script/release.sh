#!/usr/bin/env bash

VERSION_NEXT=${1?"Usage: $0 VERSION_NEXT"}
VERSION_CURRENT="$( grep "^version" build.gradle |
                    grep -oP "version \K[\w\.']+" |
                    sed "s/'//g" )"

for file in "build.gradle" \
            "docs/badge-jetbrains-website.svg" \
            "src/main/resources/META-INF/plugin.xml"
do
    sed "s/${VERSION_CURRENT}/${VERSION_NEXT}/g" ${file} > ${file}.next && mv ${file}.next ${file}
done

