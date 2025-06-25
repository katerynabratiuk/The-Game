@echo off
jpackage ^
  --input target/ ^
  --name Game-Executable ^
  --main-jar game-client.jar ^
  --main-class org.client.UDPClient ^
  --type app-image ^
  --win-console