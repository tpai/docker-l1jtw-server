#!/bin/bash
# Program:
# 	The program will show it's name and first 3 parameters.
# History:
# 2005/08/25	VBird	First release
PATH=.:/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/local/jdk1.7.0/bin
export PATH

java -Xmx512m -Xincgc -cp l1jserver.jar:./lib/c3p0-0.9.1.1.jar:./lib/javolution.jar:./lib/mysql-connector-java-5.1.5-bin.jar l1j.server.Server
