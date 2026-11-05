#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
: "${ANDROID_HOME:?Set ANDROID_HOME to your Android SDK directory}"
bt="$ANDROID_HOME/build-tools/35.0.0"
platform="$ANDROID_HOME/platforms/android-35/android.jar"
mkdir -p build/classes build/dex
if command -v javac >/dev/null; then
 javac -source 8 -target 8 -bootclasspath "$platform:$bt/core-lambda-stubs.jar" -d build/classes app/src/main/java/com/karavan/ates/*.java
else
 : "${ECJ_JAR:?Install JDK or set ECJ_JAR to Eclipse ecj 3.39.0 jar}"
 java -jar "$ECJ_JAR" -source 8 -target 8 -bootclasspath "$platform:$bt/core-lambda-stubs.jar" -d build/classes app/src/main/java/com/karavan/ates/*.java
fi
"$bt/d8" --lib "$platform" --output build/dex build/classes/com/karavan/ates/*.class
sed 's/<manifest /<manifest package="com.karavan.ates" /' app/src/main/AndroidManifest.xml > build/AndroidManifest.xml
"$bt/aapt" package -f -M build/AndroidManifest.xml -I "$platform" -F build/unsigned.apk --min-sdk-version 26 --target-sdk-version 35 --version-code 3 --version-name 1.2
(cd build/dex && zip -q -u ../unsigned.apk classes.dex)
"$bt/zipalign" -f 4 build/unsigned.apk build/aligned.apk
key="${ANDROID_DEBUG_KEYSTORE:-$HOME/.android/debug.keystore}"
if [ ! -f "$key" ]; then
 mkdir -p "$(dirname "$key")"
 keytool -genkeypair -keystore "$key" -storepass android -alias androiddebugkey -keypass android -dname 'CN=Android Debug,O=Android,C=US' -keyalg RSA -keysize 2048 -validity 10000
fi
"$bt/apksigner" sign --ks "$key" --ks-pass pass:android --out build/karavan-ates.apk build/aligned.apk
"$bt/apksigner" verify --verbose build/karavan-ates.apk
