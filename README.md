[![Build Status](https://travis-ci.org/jingx23/HarmonyControl.svg?branch=master)](https://travis-ci.org/jingx23/HarmonyControl)

# Harmony Web Control
Simple Webinterface for communicating with a Harmony Hub

![Example](https://github.com/jingx23/HarmonyControl/raw/master/webinterface.png "Example")

Based on the following projects
* https://github.com/scireum/sirius-web
* https://github.com/tuck182/harmony-java-client

Add your Logitech Harmony settings to the application.conf
```
harmony.ip="192.168.0.10"
harmony.user="Harmony Username"
harmony.password="Harmony Password"
```
or pass it via arguments
```
-Dharmony.ip="192.168.0.10" -Dharmony.user="Harmony Username" -Dharmony.password="Harmony Password"
```

TODO:
- ~~List commands~~
- ~~Send commands to the hub~~
- ~~Power on/off~~
- ~~Switch activity~~
- Make a release