#!/bin/bash
java -agentlib:jdwp=transport=dt_socket,server=y,address=9653,suspend=n -jar ../jar/CorporateServer-0.0.1-SNAPSHOT.jar & echo $! > ./pid.file &
