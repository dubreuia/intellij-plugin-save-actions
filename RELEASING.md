# Releasing

## Branches and versions

The code branches are based on "since-build" properties (except master).

- Branch `idea-version-2016-3`: tags as **1.5.0+2016.3** for version **1.5.0** and idea version **2016.3**
    - tag and github release: **1.5.0+2016.3**
    - build.gradle: `version: '2016.3'`
    - plugin.xml: `<idea-version since-build="163" until-build="183"/>`
- Branch `idea-version-2018-3`: tags as **1.5.0+2018.3** for version **1.5.0** and idea version **2018.3**
    - tag and github release: **1.5.0+2018.3**
    - build.gradle: `version: '2018.3'`
    - plugin.xml: `<idea-version since-build="183"/>` (no until)
- Branch `idea-version-2019-1`: tags as **1.5.0+2019.1** for version **1.5.0** and idea version **2019.1**
    - tag and github release: **1.5.0+2019.1**
    - build.gradle: `version: '2019.1'`
    - plugin.xml: `<idea-version since-build="191" until-build="192.*"/>`
- Branch `master` with no version or tags, idea version **2019.1** (LATEST), no release of this branch
    - build.gradle: `version: '2019.1'`
    - plugin.xml: `<idea-version since-build="191"/>`

## Reporting

- Commit on master
- Commits are then cherry-picked on `idea-version-2016-3`, `idea-version-2018-3`, and `idea-version-2019-1`

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

# For branch 2019-1, change versions, commit
git checkout idea-version-2019-1
./script/release_version.sh 1.5.0
git commit -a -m "Promote to version 1.5.0+2019.1"

# For the master branch
git checkout master
./script/release.sh 1.5.0
git commit -a -m "Promote to version 1.5.0"
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

# For branch 2019-1, change versions, commit
git checkout idea-version-2019-1
./script/build_plugin.sh

# You'll have the files locally
ls *.jar
# intellij-plugin-save-actions-1.5.0+2016.3.jar
# intellij-plugin-save-actions-1.5.0+2018.3.jar
# intellij-plugin-save-actions-1.5.0+2019.1.jar
```


