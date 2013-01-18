Agora
=====

It's like IRC met iMessages

At least that's the idea. Agora is in development. In the sence that I have a couple of proofs of concepts but no product at all.

C Programs
----------
There are two very limited programs, a client and a server. The server listens for messages on UDP port 61803 and echos them (as '<ipaddr>: <message>'). The client waits for up to 255 characters followed by a return, sends those characters to the server, and then waits again. To build these progams, compile and run (e.g. `gcc client/main.c -o client; ./client <hostname>`). Both programs require that you pass them a hostname at runtime. The hostname should be a hostname that the computer running the server can be accessed at.

Clojure Server
--------------
The clojure component is also a server and a client, though of a better design. You run the server by running `lein run` in the directory Clojure/ws/ws-intro (you have to have leiningen). To use the client, open Clojure/ws/ws-intro-client/index.html in a browser (if the user is not running on localhost, you will have to change the address at the end of main.js). To 'login', type a nickname in the bottom left field and hit enter. To send a message, type a message in the bottom (right-ish) field and hit enter.
