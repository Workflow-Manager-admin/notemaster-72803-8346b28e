#!/bin/bash
cd /home/kavia/workspace/code-generation/notemaster-72803-8346b28e/android_frontend_workspace/android_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

