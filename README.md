# Ricart-Agrawala-Algorithm


![Alt text](https://github.com/cai866/Ricart-Agrawala-Algorithm-/blob/master/project%20description/picture1.png)

![Alt text](https://github.com/cai866/Ricart-Agrawala-Algorithm-/blob/master/project%20description/picture2.png)



There are five clients and three servers.The Driver.calss and RicartAgrawalaAlg.class will run on the five different clients synchronously, which can let the five clients build the communication channel with the socket between each pair of clients, and then exchange the request and reply messages.The client can enter CS and operate the server file after receiving all replys.
The ReadFile.class and server1.class will run on the three servers separately. ReadFile.calss is able to call a readFile() function to read the last line of the objective file on server and return a string. server1.class will assign the thread for each connection quest from clients.These threads can help read file and send data to client through the socket.It also can receive information from clients and write that into server file.