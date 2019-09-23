# Releasing

## Branches and versions

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

To diff the content of the master and one backport branch, use `git diff master HEAD`, the diff content should be only
the versions, plus any feature that is not in the branch.

To know which commit to cherry-pick, use the last release tag in each branch (including master).

- Commit (or merge PR) on master
- Cherry-pick on those branches:
    - `idea-version-2016-3`
    - `idea-version-2018-3`
    - `idea-version-2019-3`

## Releasing & Packaging

To release a new version, for example **x.y.z**, from previous version **a.b.c**, on branch **branch**, with idea version **idea-version**:

```bash
git checkout branch
./script/release.sh a.b.c x.y.z idea-version
```

