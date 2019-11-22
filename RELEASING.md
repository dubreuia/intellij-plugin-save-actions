# Releasing

## Branches and versions

- Branch `idea-version-2016-3`: tags as **x.y.z+2016.3** for version **x.y.z**
  and idea version **2016.3**
    - tag and github release: **x.y.z+2016.3**
    - build.gradle: `version: '2016.3'`
    - plugin.xml: `<idea-version since-build="163" until-build="183"/>`
- Branch `idea-version-2018-3`: tags as **x.y.z+2018.3** for version **x.y.z**
  and idea version **2018.3**
    - tag and github release: **x.y.z+2018.3**
    - build.gradle: `version: '2018.3'`
    - plugin.xml: `<idea-version since-build="183" until-build="193"/>`
- Branch `idea-version-2019-3`: tags as **x.y.z+2019.3** for version **x.y.z**
  and idea version **2019.3**
    - tag and github release: **x.y.z+2019.3**
    - build.gradle: `version: '2019.3'`
    - plugin.xml: `<idea-version since-build="193"/>` (no until)
- Branch `master` with no version or tags, idea version **2019.3** (LATEST), no
  release of this branch
    - build.gradle: `version: '193-EAP-SNAPSHOT'`
    - plugin.xml: `<idea-version since-build="193"/>`

## Backporting

Diffing the content of the master and one backport branch using `git diff
master HEAD` from the backport branch.  The diff content should be only the
versions, plus any feature that is not in the branch.

Easiest way of backporting is listing the commit ids, and then using 
`git cherry-pick sha1 sha1 ...`.

To find which commit to cherry-pick, use the last release tag in each branch
(including master).

- Commit (or merge PR) on master
- Cherry-pick on those branches:
    - `idea-version-2016-3`
    - `idea-version-2018-3`
    - `idea-version-2019-3`

## Releasing & Packaging

To release and package a new version, for example **x.y.z**, from previous
version **a.b.c**:

```bash
git checkout master
./script/release.sh a.b.c x.y.z

git checkout idea-version-2019-3
./script/release.sh a.b.c x.y.z 2019.3

git checkout idea-version-2018-3
./script/release.sh a.b.c x.y.z 2018.3

git checkout idea-version-2016-3
./script/release.sh a.b.c x.y.z 2016.3
```

Then follow the link for the github release page to create manually.

## Uploading to jetbrains

Only idea versions get released (not master). Go to
https://plugins.jetbrains.com/plugin/edit?pluginId=7642 and upload
each build individually.

