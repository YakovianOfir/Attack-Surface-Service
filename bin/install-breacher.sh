#!/usr/bin/env bash
 
# Abort if not super user
if [[ ! `whoami` = "root" ]]; then
    echo "You must have administrative privileges to run this script"
    echo "Try 'sudo ./install-breacher.sh'"
    exit 1
fi

 # Analyze the directory of the script
 SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

 # Make a temporary directory to use within $SCRIPT_DIR
 WORKING_DIR="$( mktemp -d -p "$SCRIPT_DIR" )"

 # Check if the temporary directory was successfully created
 if [[ ! "$WORKING_DIR" || ! -d "$WORKING_DIR" ]]; then
   echo "Could not create working directory. Aborting"
   exit 2
 fi

 # Delete the workspace directory upon exit
 function cleanup {
   rm -rf "$WORKING_DIR"
   echo "Deleted the workspace directory $WORKING_DIR"
 }

 # Register the cleanup function to be called on the EXIT signal
 trap cleanup EXIT

# Navigate to installation folder of the project
cd "$SCRIPT_DIR"/install

# Download the Java Development Kit [version 17]
sh ./install_jdk.sh 17 jdk-17_linux-x64_bin.tar.gz "$WORKING_DIR"

# Install the Java Development Kit [version 17]
sh ./install_java.sh "$WORKING_DIR/jdk-17_linux-x64_bin.tar.gz"

# Affirm completion and exit
echo "Breacher backing packages were successfully installed!"
exit 0