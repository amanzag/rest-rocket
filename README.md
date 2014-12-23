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
