#!/usr/bin/env bash
set -euo pipefail

APP_NAME="${APP_NAME:-pokhara-bites}"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TOMCAT_HOME="${TOMCAT_HOME:-/Applications/XAMPP/apache-tomcat-10.1.28}"
WEBAPP_DIR="$ROOT_DIR/src/main/webapp"
RESOURCES_DIR="$ROOT_DIR/src/main/resources"
TARGET_DIR="$ROOT_DIR/target"
BUILD_DIR="$TARGET_DIR/xampp-war"
SOURCES_LIST="$TARGET_DIR/xampp-sources.txt"
WAR_FILE="$TARGET_DIR/$APP_NAME.war"

if [[ ! -d "$TOMCAT_HOME" ]]; then
    echo "Tomcat not found at: $TOMCAT_HOME" >&2
    echo "Set TOMCAT_HOME to your XAMPP Tomcat directory and run again." >&2
    exit 1
fi

mkdir -p "$TARGET_DIR"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/WEB-INF/classes"

cp -R "$WEBAPP_DIR"/. "$BUILD_DIR"/
(
    cd "$ROOT_DIR"
    find src/main/java -name '*.java' | sort > "$SOURCES_LIST"
)

CLASSPATH="$TOMCAT_HOME/lib/servlet-api.jar:$TOMCAT_HOME/lib/jsp-api.jar:$TOMCAT_HOME/lib/el-api.jar:$WEBAPP_DIR/WEB-INF/lib/*"
(
    cd "$ROOT_DIR"
    javac --release 11 -encoding UTF-8 -cp "$CLASSPATH" -d "$BUILD_DIR/WEB-INF/classes" @"$SOURCES_LIST"
)
cp "$RESOURCES_DIR/database.sql" "$BUILD_DIR/WEB-INF/classes/database.sql"

(
    cd "$BUILD_DIR"
    jar cf "$WAR_FILE" .
)

echo "Built $WAR_FILE"

if [[ "${1:-}" == "--deploy" ]]; then
    WEBAPPS_DIR="$TOMCAT_HOME/webapps"
    cp "$WAR_FILE" "$WEBAPPS_DIR/$APP_NAME.war"
    echo "Deployed to $WEBAPPS_DIR/$APP_NAME.war"
fi
