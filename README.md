# LegitSlimePaper - An AdvancedSlimePaper fork with command related features needed for the Legitimoose server.

## Usage

Apply patches with `./gradlew applyAllPatches` and then run a dev server with `./gradlew runServer`. You can generate a mojmap jar with `./gradlew createMojmapBundlerJar`.


## How to contribute
1. You first need to have the currently applied patches with `./gradlew applyAllPatches` and then you can start working on the code.
2. Then, you can edit the code in the `legitslimepaper-server/` folder
3. After you are done, you can save your changes to the "local git repo" with `./gradlew fixupMinecraftSourcePatches` (The task name may depend on what you are working on, eg `fixupPaperServerFilePatches`. Calling more than you need should be fine even if it throws a git error.)
4. Once all the changes are in the "local git repo", you can update the patch files with `./gradlew rebuildAllServerPatches`.
5. Finally, you can create a pull request with your changes. (You normally should only be commiting patch files. Please don't commit anything from `legitslimepaper-server/src/minecraft/`!!!)

### Small notes
- If you need to change the `build.gradle.kts.patch` files, using `./gradlew rebuildSlimeworldmanagerSingleFilePatches` will automatically update all these patch files after modifications were made.
- The `core` and `api` modules refer to ASP's custom modules. They are not directly related to paper.

## `Gradle patch task failed with exit code 128“` on Windows?
This error occurs because the default Windows path limit is 255 characters. There cannot be paths longer than 255 characters, which is obviously bad, especially if you have many nested folders. Change the following key in the [Windows Registry](https://en.wikipedia.org/wiki/Windows_Registry) to fix it:
```
HKLM\SYSTEM\CurrentControlSet\Control\FileSystem\LongPathsEnabled - Set to 1
```
Make sure to also allow long paths in git:
```
git config --system core.longpaths true
```

For more information, refer to [PaperMC/paperweight-examples](https://github.com/PaperMC/paperweight-examples) and [PaperMC/Paper/CONTRIBUTING.md](https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md). (The CONTRIBUTING.md also explains how to set up the development environment)