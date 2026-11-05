#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
: "${ECJ_JAR:?Set ECJ_JAR to Eclipse ecj 3.39.0 jar}"
: "${JSON_TEST_JAR:?Set JSON_TEST_JAR to org.json 20240303 jar}"
mkdir -p build/host-tests
java -jar "$ECJ_JAR" -21 -cp "$JSON_TEST_JAR" -d build/host-tests \
 tests/host/android/content/*.java tests/host/com/karavan/ates/Timers.java \
 app/src/main/java/com/karavan/ates/TimerMath.java \
 app/src/main/java/com/karavan/ates/BusinessClock.java \
 app/src/main/java/com/karavan/ates/Ledger.java \
 tests/TimerMathTest.java tests/BusinessClockTest.java tests/host/LedgerTest.java
for suite in TimerMathTest BusinessClockTest LedgerTest; do
 java -cp "build/host-tests:$JSON_TEST_JAR" "$suite"
done
