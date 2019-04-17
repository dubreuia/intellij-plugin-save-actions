# Releasing

## Branches and versions

The code branches are

- Branch `master` with no version / tags and idea version **2019.1** (LATEST)
    - build.gradle: `version: '2019.1'`
- Branch `idea-version-2016-3` : tags as **1.5.0+2016.3** for version **1.5.0** and idea version **2016.3**
    - github milestone / release: **1.5.0+2016.3**
    - build.gradle: `version: '2016.3'`
    - plugin.xml: `<idea-version since-build="163.0" until-build="191.0"/>`
- Branch `idea-version-2018-3` : tags as **1.5.0+2018.3** for version **1.5.0** and idea version **2018.3**
    - github milestone / release: **1.5.0+2018.3**
    - build.gradle: `version: '2018.3'`
    - plugin.xml: `<idea-version since-build="191.0"/>`

## Reporting

- Commits go on master
- Commits are then cherry-picked on `idea-version-2016-3` and `idea-version-2018-3`
- No merge please

## Releasing

To release a new version, for example **1.5.0**, run

```bash
# For branch 2016-3, change versions, commit
git checkout idea-version-2016-3
./script/release_version.sh 1.5.0
git commit -a -m "Promote to version 1.5.0+2016.3"

# For branch 2018-3, change versions, commit
git checkout idea-version-2018-3
./script/release_version.sh 1.5.0
git commit -a -m "Promote to version 1.5.0+2018.3"
```

Then manually create a new version in [github](https://github.com/dubreuia/intellij-plugin-save-actions/releases/new).

## Packaging

To package a new version, for example **1.5.0**, run

```bash
# For branch 2016-3, change versions, commit
git checkout idea-version-2016-3
./script/build_plugin.sh

# For branch 2018-3, change versions, commit
git checkout idea-version-2018-3
./script/build_plugin.sh

# You'll have the files locally
ls *.jar
# intellij-plugin-save-actions-1.5.0+2016.3.jar
# intellij-plugin-save-actions-1.5.0+2018.3.jar
```


