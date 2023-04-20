# Knitting Markup
This is a language designed to closely match the way existing knitting patterns write instructions.

# How to run
## JavaCC Gradle Plugin
This project is built with JavaCC using the [JavaCC gradle plugin](https://github.com/javacc/javaccPlugin) which handles compiling the `.jj` source files and copying generated `.java` source to the right directory.

Since the current version in maven central is outdated (`2.4.0` from 2016), I had to build the newer version locally.

Clone [this](https://github.com/javacc/javaccPlugin) project and run the `publishToMavenLocal` publishing task to store the file in your local .m2 folder.

## Main Application
Build the application with `gradlew build`.

The main method can be found in `dev.ryanramsdell.Main`, run this application as you would any other java app.

## Standalone Parser
TBD if I want to expose this