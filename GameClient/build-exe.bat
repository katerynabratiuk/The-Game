@echo off
jpackage ^
  --input target/ ^
  --name Game-Executable ^
  --main-jar game-client.jar ^
  --main-class org.client.GameClient ^
  --type app-image ^
  --win-console