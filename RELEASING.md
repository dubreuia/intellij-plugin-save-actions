# Releasing

## Branches and versions

Maintained:

- Branch `master` with no version or tags, idea version **2019.3** (LATEST), no release of this branch
    - tag and github release: **x.y.z**
    - build.gradle: `version: 'LATEST-EAP-SNAPSHOT`
    - plugin.xml: `<idea-version since-build="183"/>` (since 2018.3)

Not maintained:

- Branch `idea-version-2016-3`: tags as **x.y.z+2016.3** for version **x.y.z** and idea version **2016.3**
    - tag and github release: **x.y.z+2016.3**
    - build.gradle: `version: '2016.3'`
    - plugin.xml: `<idea-version since-build="163" until-build="183"/>`
    - last version: 1.7.0+2016.3
- Branch `idea-version-2018-3`: tags as **x.y.z+2018.3** for version **x.y.z** and idea version **2018.3**
    - tag and github release: **x.y.z+2018.3**
    - build.gradle: `version: '2018.3'`
    - plugin.xml: `<idea-version since-build="183" until-build="193"/>`
    - last version: 1.9.0+2016.3
- Branch `idea-version-2019-3`: tags as **x.y.z+2019.3** for version **x.y.z** and idea version **2019.3**
    - tag and github release: **x.y.z+2019.3**
    - build.gradle: `version: '2019.3'`
    - plugin.xml: `<idea-version since-build="193"/>` (no until)
    - last version: 1.9.0+2016.3

## Backporting

We are not backporting anymore in 2016, 2018, and 2019 branches.

## Releasing & Packaging

To release and package a new version, for example **x.y.z**, from previous version **a.b.c**:

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

git checkout master
./script/release.sh a.b.c x.y.z
```

Then follow the link for the github release page to create manually.

## Uploading to jetbrains

Only idea versions get released (not master). Go to https://plugins.jetbrains.com/plugin/edit?pluginId=7642 and upload each build individually.
