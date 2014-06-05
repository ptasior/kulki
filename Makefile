all: build install

build:
	ant debug

install:
	adb uninstall com.games.kulki
	adb install bin/Kulki-debug.apk

