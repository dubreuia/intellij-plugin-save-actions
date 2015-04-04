# Intellij Save Actions Plugin

Supports configurable, Eclipse like, save actions, including "organize imports", "reformat code" and "rearrange code". The plugin executes the configured actions when the file is synchronised (or saved) on disk.

## Features

- Organize imports
- Reformat code (only changed text or all)
- Rearrange code (reorder methods, fields, etc.)
- Simple option page to activate / deactivate actions

## Installation

Install it from the plugin repository in Intellij IDEA:

- "File > Settings > Plugins > Browse repositories... > Search 'Save Actions' > Category 'Code tools'"

The plugin is available from the [Intellij IDEA Community Edition plugin repository](https://plugins.jetbrains.com/plugin/7642) and you can also download the jar from there then add it in Intellij IDEA:

- "File > Settings > Plugins > Install plugin from disk..."

## Configuration

Activate the plugin in Intellij IDEA:

- "File > Settings > Other Settings > Save Actions"
- Then configure the actions you need (import, reformat, etc.)

If you want to see / configure the rearrangement options:

- "File > Settings > Code Style > Java > Arrangement"

## Bugs / features

Dat plugin do not work ? You can [ask me on twitter](https://twitter.com/dubreuia) or [create an issue on github](https://github.com/dubreuia/intellij-plugin-save-actions/issues).

## Compatibility

This plugin works with Java 6+ and Intellij IDEA Community Edition after 131 (around version 13).

## Licence

[MIT License](LICENSE.txt)
