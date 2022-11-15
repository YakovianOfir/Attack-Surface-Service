#!/usr/bin/env bash
 
# Abort if not super user
if [[ ! `whoami` = "root" ]]; then
    echo "You must have administrative privileges to run this script"
    echo "Try 'sudo ./install-java.sh <jdk-archive-path>'"
    exit 1
fi
 
# Check that the file is a JDK archive
if [[ ! $1 =~ jdk-[0-9]{1}u[0-9]{1,2}.*\.tar\.gz ]]; then
    echo "'$1' doesn't look like a JDK archive."
    echo "The file name should begin 'jdk-XuYY', where X is the version number and YY is the update number."
    echo "Please re-name the file, or download the JDK again and keep the default file name."
    exit 2
fi
 
# Unpack the JDK, and get the name of the unpacked folder
tar -xf $1
JDK_VER=`echo $1 | sed -r 's/jdk-([0-9]{1}u[0-9]{1,2}).*\.tar\.gz/\1/g'`
JDK_NAME=`echo $JDK_VER | sed -r 's/([0-9]{1})u([0-9]{1,2})/jdk1.\1.0_\2/g'`
 
# Create a location to save the JDK, and move it there
mkdir /usr/local/java
touch /usr/bin/java /usr/bin/javac /usr/bin/javaws /usr/bin/jar
mv $JDK_NAME /usr/local/java
JAVA_HOME=/usr/local/java/$JDK_NAME
 
# Place links to java commands in /usr/bin, and set preferred sources
update-alternatives --install "/usr/bin/java" "java" "$JAVA_HOME/bin/java" 1
update-alternatives --set "java" "$JAVA_HOME/bin/java"
 
update-alternatives --install "/usr/bin/javac" "javac" "$JAVA_HOME/bin/javac" 1
update-alternatives --set "javac" "$JAVA_HOME/bin/javac"
 
update-alternatives --install "/usr/bin/javaws" "javaws" "$JAVA_HOME/javaws" 1
update-alternatives --set "javaws" "$JAVA_HOME/bin/javaws"
 
update-alternatives --install "/usr/bin/jar" "jar" "$JAVA_HOME/bin/jar" 1
update-alternatives --set "jar" "$JAVA_HOME/bin/jar"
 
# Affirm completion, optionally delete archive, and exit
echo "Java Development Kit [version $JDK_VER] was successfully installed."
exit 0