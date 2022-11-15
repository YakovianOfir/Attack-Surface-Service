#!/usr/bin/env bash
 
# Abort if not super user
if [[ ! `whoami` = "root" ]]; then
    echo "You must have administrative privileges to run this script"
    echo "Try 'sudo ./install-jdk.sh <jdk-version> <jdk-archive-name> <target-folder>'"
    exit 1
fi

# Download the JDK to the required destination folder
wget https://download.oracle.com/java/"$1"/latest/"$2" -P "$3"

# Affirm completion and exit
echo "Java Development Kit [version $1] was successfully downloaded."
exit 0