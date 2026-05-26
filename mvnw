#!/bin/sh

# Find directory containing this script
DIRNAME=$(dirname "$0")
WRAPPER_JAR="$DIRNAME/.mvn/wrapper/maven-wrapper.jar"

# Find Java
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

# Download wrapper jar if not exists
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper jar..."
    mkdir -p "$DIRNAME/.mvn/wrapper"
    
    # Try curl first, fallback to wget
    if command -v curl >/dev/null 2>&1; then
        curl -sL -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    elif command -v wget >/dev/null 2>&1; then
        wget -q -O "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    else
        echo "Error: Neither curl nor wget found. Please download the maven-wrapper jar manually."
        exit 1
    fi
fi

# Run wrapper
exec "$JAVACMD" $MAVEN_OPTS -classpath "$WRAPPER_JAR" "-Dmaven.multiModuleProjectDirectory=$DIRNAME" org.apache.maven.wrapper.MavenWrapperMain "$@"
