all: permission compile

permission:
	chmod 755 ./L1J-TW_3.50c/build/ant/bin/ant

compile:
	cd L1J-TW_3.50c; ./build/ant/bin/ant
