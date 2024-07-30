#!/bin/bash
git clone https://github.com/Mrredstone5230/LegitSlimePaper.git
cd LegitSlimePaper
git checkout fix/build-version
git submodule init
git submodule update
chmod +x gradlew
./gradlew applyPatches
./gradlew build
./gradlew createMojmapPaperclipJar
