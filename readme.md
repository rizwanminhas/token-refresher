## About
A simple application that shows how to allow reads and writes to a variable in a thread safe way by using Akka.

## Details.
1. The `MainApp` **generates** a token every 3 seconds.
2. The `MainApp` **prints** (send pattern) the token value every 300 milliseconds in the Actor itself.
3. The `MainApp` **gets** (ask pattern) the token value every 300 milliseconds, converts it to a Scala future and prints it in the MainApp itself. 