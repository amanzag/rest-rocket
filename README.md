rest-rocket
===========

REST controller for Dream Cheeky THUNDER rocket launcher

# Building

Prerequisites:
- Java 8
- Maven 3

In order to build it, just run:
$ mvn package

# Running

$ java [-Dport=$PORT_NUMBER] -jar target/rest-rocket-$VERSION-jar-with-dependencies.jar

Then just open the following in a browser:
http://host:8080

# RESTful API

In addition to the web frontend, the application provides a rest APIi (all POST):
- /api/rocket/LEFT
- /api/rocket/RIGHT
- /api/rocket/UP
- /api/rocket/DOWN
- /api/rocket/LEDON
- /api/rocket/LEDOFF
- /api/rocket/FIRE

# Camera

You can attach a webcam to your rocket launcher and the app will try to auto-detect it. When a camera is found, the application will grab images periodically and send them to the browser.

You can modify the behaviour with some parameters:
- -DdisableCamera=true --> disables camera support. Won't try to auto-detect it.
- -Dcamera=N --> will try to use device N (useful if you have more than one camera). Defaults to 0.
- 
