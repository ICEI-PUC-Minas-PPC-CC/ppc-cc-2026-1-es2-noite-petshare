#!/usr/bin/env sh
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIR/.mvn/wrapper/maven-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
	echo "Downloading maven-wrapper.jar..."
	mkdir -p "$DIR/.mvn/wrapper"
	curl -fsSL -o "$WRAPPER_JAR" "https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar"
fi
exec java -jar "$WRAPPER_JAR" "$@"
