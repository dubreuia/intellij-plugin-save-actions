# Intellij Save Actions Plugin

Supports configurable, Eclipse like, save actions, including "organize imports", "reformat code" and "rearrange code". The plugin executes the configured actions when the file is synchronised (or saved) on disk.

## Features

- Organize imports
- Reformat code (only changed text or all)
- Rearrange code (reorder methods, fields, etc.)
- Works on any file type Intellij IDEA can reformat (Java, XML, etc.)
- Simple option page to activate / deactivate actions

## Installation

Install it from the plugin repository in Intellij IDEA:

- "File > Settings > Plugins > Browse repositories... > Search 'Save Actions' > Category 'Code tools'"

The plugin is available from the [Intellij IDEA Community Edition plugin repository](https://plugins.jetbrains.com/plugin/7642) and you can also download the jar from there then add it in Intellij IDEA:

- "File > Settings > Plugins > Install plugin from disk..."

## Configuration

The configurations are located in "File > Settings > Other Settings > Save Actions".

### Activate save actions 

Enable / disable the plugin.

### Organize imports

Enable / disable import organization (configured in "File > Settings > Code Style > Java > Imports").

### Reformat code

Enable / disable formatting (configured in "File > Settings > Code Style"). See "Reformat only changed code" for more options.

### Rearrange code

Enable / disable re-ordering of fields and methods (configured in "File > Settings > Code Style > Java > Arrangement").

### Reformat only changed code

Enable / disable formatting for changed code only. If VCS is configured, it is used to check which lines where modified. If VCS is not configured, the code will always get reformatted.

### File path exclusions

Add / remove file path exclusions to ignore files. The entries are Java regular expressions, and it matches the whole file name from the project root. A pattern that do not compile cannot be added to the list. Some examples: 

Exclude 'Main.java' only in root folder:

    Main\.java
    
Exclude file 'Foo.java' only in folder 'src':
    
    src/Foo\.java

Exclude all xml files in any folder:

    .*/.*\.xml

## Bugs / features

Dat plugin do not work ? You can [ask me on twitter](https://twitter.com/dubreuia) or [create an issue on github](https://github.com/dubreuia/intellij-plugin-save-actions/issues).

## Compatibility

This plugin works with Java 6+ and Intellij IDEA Community Edition build 135+ (version 13.1.6).

## Contributors

- [krasa](https://github.com/krasa)

## Licence

[MIT License](LICENSE.txt)
