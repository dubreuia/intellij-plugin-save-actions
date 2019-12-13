# Contributing

## Development environment

The plugin is built with gradle, but you don't need to install it if you build with the Intellij gradle plugin (check out the [prerequisites](https://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system/prerequisites.html)).  If you don't intend to use the Intellij gradle plugin, you can use native gradle (replace `./gradlew` by `gradle`).

Start idea and import the `build.gradle` file with "File > Open". Then in the "Import Project from Gradle" window, make sure you check "Use gradle 'wrapper' task configuration" before clicking "Finish". You now have a gradle wrapper installed (`gradlew`) that you can use on the command line to generate idea folders:

```bash
# Initialize idea folders
./gradlew cleanIdea idea
```

Intellij should refresh and the project is now configured as a gradle project. You can find Intellij gradle tasks in "Gradle > Gradle projects > intellij-plugin-save-actions > Tasks > intellij". To run the plugin, use the `runIde` task:

```bash
# Run the plugin (starts new idea)
./gradlew runIde
```

### Java version (11)

The plugin requires Java 11 to build. On Ubuntu, you can install OpenJDK using `sudo apt install openjdk-11-jdk`. The Gradle wrapper needs the environment variable `JAVA_HOME` to work, or the -D flag, for example:

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Or use the -D flag
gradle build -D org.gradle.java.home=/usr/lib/jvm/java-11-openjdk-amd64
```

During the Intellij setup, you will need to manually set the Java version in "Gradle Settings", since it doesn't honor the `JAVA_HOME` property.

## Code style

The code style is located in `config/code-style.xml`, you can import it by doing "File > Settings > Editor > Code Style > Scheme > (wheel) > Import scheme > Intellij IDEA code style XML".  General style should resemble existing code.

## Sending a pull request

To contribute, submit a PR without modifying the plugin version. Before sending, think about documenting your feature, code style, unit tests (if possible), integration tests (see `com.dubreuia.integration.IntegrationTest`) and proper manual testing.

