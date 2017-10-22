# Intellij Save Actions Plugin

Supports configurable, Eclipse like, save actions, including "organize imports", "reformat code", "rearrange code", "compile file" and some quick fixes like "add / remove 'this' qualifier", etc. The plugin executes the configured actions when the file is synchronised (or saved) on disk.

Using the save actions plugin makes your code cleaner and more uniform across your code base by enforcing your code style and code rules every time you save. The settings file (see [files location](https://github.com/dubreuia/intellij-plugin-save-actions#files-location)) can be shared in your development team so that every developer has the same configuration.

The code style applied by the save actions plugin is the one configured your settings at "File > Settings > Editor > Code Style". For some languages, custom formatter may also be triggered by the save actions plugin. For example for Dart developers, enable "Use the dartfmt tool when formatting the whole file" option in "File > Settings > Editor > Code Style > Dart > Dartfmt".

## Features

- Organize imports
- Run on file save or shortcut (or both)
- Reformat code (whole file or only changed text)
- Rearrange code (reorder methods, fields, etc.)
- Include / exclude files with regex support
- Works any file type (Java, Python, XML, etc.)
- Commitable, per-projet settings
- Other IDE support (Eclipse *.epf files)
- Automatically fix Java [inspections](https://github.com/dubreuia/intellij-plugin-save-actions/#java-quick-fixes) (Intellij IDEA only)

![Save actions plugin settings page](https://github.com/dubreuia/intellij-plugin-save-actions/blob/master/docs/intellij-save-actions-plugin-settings-page.png)

## Compatibility

Built with IntelliJ IDEA IU-163.7743.44 (2016.3.6), JDK 1.6, those are the currently supported products, and is not expected to work in other products:

- Intellij IDEA
- PyCharm
- PHPStorm
- AndroidStudio

See issue [#18](https://github.com/dubreuia/intellij-plugin-save-actions/issues/18) for a beta packaging that you can try in other products.

## Installation

Install it from the plugin repository in Intellij IDEA (**recommended**):

- "File > Settings > Plugins > Browse repositories... > Search 'Save Actions' > Category 'Code tools'"

Or the plugin is available from the [Intellij IDEA Community Edition plugin repository](https://plugins.jetbrains.com/plugin/7642) and you can also download the jar from there then add it in Intellij IDEA:

- "File > Settings > Plugins > Install plugin from disk..."

## Configuration

The configurations are located in "File > Settings > Other Settings > Save Actions".

### Activation

| Name                               | Description
| ---                                | ---
| Activate save actions on file save | Enable / disable the plugin on file save. Before saving each file, it will perform the configured actions below
| Activate save actions on shortcut  | Enable / disable the plugin on shortcut, by default "CTRL + SHIFT + S" (configured in "File > Keymaps > Main menu > Code > Save Actions")
| No action if compile errors        | Enable / disable no action if there are compile errors. Applied to each file individually

### Global

| Name                               | Description
| ---                                | ---
| Organize imports                   | Enable / disable import organization (configured in "File > Settings > Code Style > Java > Imports")
| Reformat file                      | Enable / disable formatting (configured in "File > Settings > Code Style"). See "Reformat only changed code" for more options
| Rearrange fields and methods       | Enable / disable re-ordering of fields and methods (configured in "File > Settings > Code Style > Java > Arrangement")
| Reformat only changed code         | Enable / disable formatting for changed code only. If VCS is configured, it is used to check which lines where modified. If VCS is not configured, the code will always get reformatted

### Build

| Name                               | Description
| ---                                | ---
| Compile file                       | Enable / disable compiling of the modified file. The compiler might compile other files as well

### Configuration

| Name                               | Description
| ---                                | ---
| File path inclusions               | Add / remove file path inclusions (by default, everything included). The Java regular expressions match the whole file name from the project root. Include only Java files: `.*\.java`. 
| File path exclusions               | Add / remove file path exclusions to ignore files (overrides inclusions). The Java regular expressions match the whole file name from the project root. Exclude 'Main.java' only in root folder: `Main\.java`. Exclude file 'Foo.java' only in folder 'src': `src/Foo\.java`. Exclude all xml files in any folder: `.*/.*\.xml`
| Use external Eclipse configuration | Add external configuration file ".epf" to read settings from. This will update the current settings and use only the ".epf" file content. Use "reset" button to remove

### Java fixes

If a quick fix adds something that is removed by another quick fix, the removal wins.

| Name                                                    | Description
| ---                                                     | ---
| Add final to field                                      | Will add the final modifier to fields
| Add final to local variable or parameter                | Will add the final modifier to local variable and parameters
| Add this to field access                                | Will qualify all field access with this
| Add missing @Override annotations                       | Will add missing @Override annotations to all inherited methods
| Add blocks in if/while/for statements                   | Will add missing braces to any if, while or for statements without braces
| Remove unnecessary this                                 | Will remove unnecessary this field access
| Remove final from private method                        | Will remove final for private method
| Remove unnecessary final to local variable or parameter | Will remove unnecessary final to local variable or parameter
| Remove explicit generic type for diamond                | Will remove unused right side generic types for Java 7 diamond operator. This `List<String> list = new ArrayList<String>()` becomes `List<String> list = new ArrayList<>()`
| Remove unused suppress warning annotation               | Will remove any unused @SuppressWarning annotations
| Remove unnecessary semicolon                            | Will remove unnecessary semicolon

## Contributing

To contribute:

- Submit a PR without modifying the META-INF/plugin.xml file (no version change)

Then the maintainer will:

- Review the change, update the version, merge to master
- Build the plugin with Intellij IDEA and test it (see compatibility section)
- Create a new release in https://github.com/dubreuia/intellij-plugin-save-actions/releases
- Upload the plugin to [JetBrains Plugins Repository](https://plugins.jetbrains.com/) 

### Contributors

- [markiewb](https://github.com/markiewb)
- [krasa](https://github.com/krasa)
- [dorkbox](https://github.com/dorkbox)
- [zhujk](https://github.com/zhujk)
- [marcosbento](https://github.com/marcosbento)

## Jetbrains plugin page

The plugin is in the [JetBrains plugin repository](https://plugins.jetbrains.com/plugin/7642-save-actions).

## Rate this plugin

Please take the time to [rate the plugin](https://plugins.jetbrains.com/plugin/7642-save-actions)!

## Files location

- **idea.log**: The log file the save actions plugin writes in. It contains debug information, prefixed with `com.dubreuia.SaveActionManager`. If you are using default locations, it would be in `~/.IntelliJIdeaVERSION/system/log/idea.log`.
- **saveactions_settings.xml**: The settings file is saved by project in the `.idea` folder. That file can be committed in git thus shared in your development team. If you are using the default locations, it would be in `~/IdeaProjects/PROJECT_NAME/.idea/saveactions_settings.xml`

## Bugs / features

The plugin does not work? You want more features? You can [ask me on twitter](https://twitter.com/dubreuia) or [create an issue on github](https://github.com/dubreuia/intellij-plugin-save-actions/issues).

## Licence

[MIT License](LICENSE.txt)
