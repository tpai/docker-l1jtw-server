all: v350c v380c

v350c: permission_350c compile_350c
v380c: permission_380c compile_380c

permission_350c:
	chmod 755 ./L1J-TW_3.50c/build/ant/bin/ant

compile_350c:
	cd L1J-TW_3.50c; ./build/ant/bin/ant

permission_380c:
	chmod 755 ./L1J-TW_3.80c/build/ant/bin/ant

compile_380c:
	cd L1J-TW_3.80c; ./build/ant/bin/ant
