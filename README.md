# Key Logger
- Java Based key logger
- Send keys pressed to a server using POST

Keys are tracked using the jnativehook library. Keys are collected native to the system and buffered in sets of 5 keys. Once 5 keys collected a POST request is sent. POST request also sent on termination with shutdown hook to send keys left in buffer. Keys sent as strings. Beginning and Ending markers are also sent to show when Keylogger began and when it ended to track sessions. 

Server saves all the keys to a database and displays them on a webpage. 

![Demo](http://g.recordit.co/Cw6EEoofEY.gif)
