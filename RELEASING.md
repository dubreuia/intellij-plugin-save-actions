# Releasing

## Branches and versions

The code branches are based on "since-build" properties (except master).

- Branch `idea-version-2016-3`: tags as **x.y.z+2016.3** for version **x.y.z** and idea version **2016.3**
    - tag and github release: **x.y.z+2016.3**
    - build.gradle: `version: '2016.3'`
    - plugin.xml: `<idea-version since-build="163" until-build="183"/>`
- Branch `idea-version-2018-3`: tags as **x.y.z+2018.3** for version **x.y.z** and idea version **2018.3**
    - tag and github release: **x.y.z+2018.3**
    - build.gradle: `version: '2018.3'`
    - plugin.xml: `<idea-version since-build="183" until-build="193"/>`
- Branch `idea-version-2019-3`: tags as **x.y.z+2019.3** for version **x.y.z** and idea version **2019.3**
    - tag and github release: **x.y.z+2019.3**
    - build.gradle: `version: '2019.3'`
    - plugin.xml: `<idea-version since-build="193"/>` (no until)
- Branch `master` with no version or tags, idea version **2019.3** (LATEST), no release of this branch
    - build.gradle: `version: '193-EAP-SNAPSHOT'`
    - plugin.xml: `<idea-version since-build="193"/>`

## Backporting

To diff the content of the master and one backport branch, use `git diff master branch`, the diff content should be only
the versions, plus any feature that is not in the branch.

To know which commit to cherry-pick, use the last release tag in each branch (including master).

- Commit (or merge PR) on master
- Cherry-pick on those branches:
    - `idea-version-2016-3`
    - `idea-version-2018-3`
    - `idea-version-2019-3`

## Releasing & Packaging

To release a new version, for example **x.y.z**, run

```bash
# For branch 2016-3, change versions, commit
git checkout idea-version-2016-3
./script/release_version.sh x.y.z
git commit -a -m "Promote to version x.y.z+2016.3"
git push
# Package
./script/build_plugin.sh

# For branch 2018-3, change versions, commit
git checkout idea-version-2018-3
./script/release_version.sh x.y.z
git commit -a -m "Promote to version x.y.z+2018.3"
git push
# Package
./script/build_plugin.sh

# For branch 2019-3, change versions, commit
git checkout idea-version-2019-3
./script/release_version.sh x.y.z
git commit -a -m "Promote to version x.y.z+2019.3"
git push
# Package
./script/build_plugin.sh

# For the master branch
git checkout master
./script/release.sh x.y.z
git commit -a -m "Promote to version x.y.z"
git push
# Package
./script/build_plugin.sh

# You'll have the files locally
ls *.jar
# intellij-plugin-save-actions-x.y.z+2016.3.jar
# intellij-plugin-save-actions-x.y.z+2018.3.jar
# intellij-plugin-save-actions-x.y.z+2019.3.jar
# intellij-plugin-save-actions-x.y.z+LATEST.jar
```

Then manually create a new version for each branch in [github](https://github.com/dubreuia/intellij-plugin-save-actions/releases/new).

