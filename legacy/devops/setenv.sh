#!/bin/bash

# Add Java options for Java 17
export JAVA_OPTS="$JAVA_OPTS \
--add-opens java.base/java.time=ALL-UNNAMED \
--add-opens java.base/java.security=ALL-UNNAMED \
--add-opens java.base/java.security.cert=ALL-UNNAMED \
--add-opens java.base/java.security.interfaces=ALL-UNNAMED \
--add-opens java.base/java.security.spec=ALL-UNNAMED"
