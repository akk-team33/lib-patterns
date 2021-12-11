#!/bin/bash

if (($# != 1)); then
  echo "usage: $0 VERSION_TAG"
  echo "example (I): $0 3.0.5-SNAPSHOT"
  echo "example (II): $0 4.0"
  exit
fi

TAG=$1

echo "You are using the tag: $TAG"
read -p "Press enter to continue"

mvn versions:set -DnewVersion=$TAG -DgenerateBackupPoms=false -DprocessAllModules=true
