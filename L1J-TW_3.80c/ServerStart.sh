#!/bin/bash
# Program:
# 	The program will show it's name and first 3 parameters.
# History:
# 2005/08/25	VBird	First release
PATH=.:/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/local/jdk1.7.0/bin
export PATH

java -Xmx512m -Xincgc -jar l1jserver.jar
