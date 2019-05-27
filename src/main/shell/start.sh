#!/bin/bash

if [ $# != 1 ] ; then
	echo "please insert folder path!"
	exit 1;
fi
filepath=$1


if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" < "1.8" ]]; then
        echo version need more than 1.7
	exit 1;
    fi
fi

${_java} -jar ./cloc.jar ${filepath}
