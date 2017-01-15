# Intellij Save Actions Plugin

Supports configurable, Eclipse like, save actions, including "organize imports", "reformat code", "rearrange code", "compile file" and some quick fixes like "add / remove 'this' qualifier", etc. The plugin executes the configured actions when the file is synchronised (or saved) on disk.

## Features

### Code formatting

- Organize imports
- Reformat code (only changed text or all)
- Rearrange code (reorder methods, fields, etc.)

### Build

- Compile file

### Quick fixes

- Add final to local variable
- Add final to field
- Remove explicit generic type for diamond
- Qualify field access with this
- Remove unused suppress warning annotation
- Remove final from private method
- Remove unnecessary semicolon
- Add missing @Override annotations

### Other

- File exclusion with regex support
- Works on any file type Intellij IDEA can reformat (Java, XML, etc.)
- Simple option page to activate / deactivate actions

## Compatibility

Intellij IDEA Community Edition build 135+

## Installation

Install it from the plugin repository in Intellij IDEA (**recommended**):

- "File > Settings > Plugins > Browse repositories... > Search 'Save Actions' > Category 'Code tools'"

Or the plugin is available from the [Intellij IDEA Community Edition plugin repository](https://plugins.jetbrains.com/plugin/7642) and you can also download the jar from there then add it in Intellij IDEA:

- "File > Settings > Plugins > Install plugin from disk..."

## Configuration

The configurations are located in "File > Settings > Other Settings > Save Actions".

### Activate save actions 

Enable / disable the plugin.

### Organize imports

Enable / disable import organization (configured in "File > Settings > Code Style > Java > Imports").

### Reformat file

Enable / disable formatting (configured in "File > Settings > Code Style"). See "Reformat only changed code" for more options.

### Rearrange fields and methods

Enable / disable re-ordering of fields and methods (configured in "File > Settings > Code Style > Java > Arrangement").

### Reformat only changed code

Enable / disable formatting for changed code only. If VCS is configured, it is used to check which lines where modified. If VCS is not configured, the code will always get reformatted.

### Compile file

Enable / disable compiling of the modified file. The compiler might compile other files as well.

### File path exclusions

Add / remove file path exclusions to ignore files. The entries are Java regular expressions, and it matches the whole file name from the project root. A pattern that do not compile cannot be added to the list. Some examples: 

Exclude 'Main.java' only in root folder:

    Main\.java
    
Exclude file 'Foo.java' only in folder 'src':
    
    src/Foo\.java

Exclude all xml files in any folder:

    .*/.*\.xml

### Add final to local variable

Will add the final modifier to any local variable (not parameters or fields).

### Add final to field

Will add the final modifier to any class fields (not parameters or local variables).

### Remove explicit generic type for diamond

Will remove unused right side generic types for Java 7 diamond operator.

    List<String> list = new ArrayList<String>();

Becomes

    List<String> list = new ArrayList<>();

### Qualify field access with this

Will qualify all field access with this.

### Remove unused suppress warning annotation

Will remove any unused @SuppressWarning annotations.

### Remove final from private method

Will remove final for private method.

### Remove unnecessary semicolon

Will remove unnecessary semicolon.

### Add missing @Override annotations

Will add missing @Override annotations to all inherited methods.

## Bugs / features

Dat plugin do not work? You want more features? You can [ask me on twitter](https://twitter.com/dubreuia) or [create an issue on github](https://github.com/dubreuia/intellij-plugin-save-actions/issues).

## Contributors

- [krasa](https://github.com/krasa)
- [dorkbox](https://github.com/dorkbox)
- [marcosbento](https://github.com/marcosbento)
- [zhujk](https://github.com/zhujk)

## Licence

[MIT License](LICENSE.txt)
