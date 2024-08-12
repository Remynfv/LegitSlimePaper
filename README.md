# LegitSlimePaper - An AdvancedSlimePaper fork with command related features needed for the Legitimoose server.

## Tasks

```
Paperweight tasks
-----------------
applyApiPatches
applyPatches
applyServerPatches
cleanCache - Delete the project setup cache and task outputs.
createMojmapBundlerJar - Build a runnable bundler jar
createMojmapPaperclipJar - Build a runnable paperclip jar
createReobfBundlerJar - Build a runnable bundler jar
createReobfPaperclipJar - Build a runnable paperclip jar
generateDevelopmentBundle
rebuildApiPatches
rebuildPatches
rebuildServerPatches
reobfJar - Re-obfuscate the built jar to obf mappings
runDev - Spin up a non-relocated Mojang-mapped test server
runReobf - Spin up a test server from the reobfJar output jar
runShadow - Spin up a test server from the shadowJar archiveFile
```

# `Gradle patch task failed with exit code 128 on Windows`?
This error occurs because the default Windows path limit is 255 characters. There cannot be paths longer than 255 characters, which is obviously bad, especially if you have many nested folders. Change the following key in the [Windows Registry](https://en.wikipedia.org/wiki/Windows_Registry) to fix it:
```
HKLM\SYSTEM\CurrentControlSet\Control\FileSystem\LongPathsEnabled - Set to 1
```
Make sure to also allow long paths in git:
```
git config --system core.longpaths true
```

For more information, refer to [PaperMC/paperweight-examples](https://github.com/PaperMC/paperweight-examples) and [PaperMC/Paper/CONTRIBUTING.md](https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md). (The CONTRIBUTING.md also explains how to set up the development environment)