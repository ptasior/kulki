all: build install

build:
	ant -emacs -quiet debug

install:
	adb uninstall com.games.kulki
	adb install bin/Kulki-debug.apk

