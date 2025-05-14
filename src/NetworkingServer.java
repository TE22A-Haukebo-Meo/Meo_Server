import java.net.*;
import java.io.*;

public class NetworkingServer {
    public static void main(String [] args) {
        ServerSocket server = null;
        Socket client;

        // Default port number we are going to use
        int portnumber = 5000;
        if (args.length > 1){
            portnumber = Integer.parseInt(args[0]);
        }

        // Create Server side socket
        try {
            server = new ServerSocket(portnumber);
        } catch (IOException ie) {
            System.out.println("Cannot open socket." + ie);
            System.exit(1);
        }
        System.out.println("ServerSocket is created " + server);

        // Wait for the data from the client and reply
        while(true) {
            try {
                // Listens for a connection to be made to
                // this socket and accepts it. The method blocks until
                // a connection is made
                System.out.println("Waiting for connect request...");
                client = server.accept();

                System.out.println("Connect request is accepted...");
                String clientHost = client.getInetAddress().getHostAddress();
                int clientPort = client.getPort();
                System.out.println("Client host = " + clientHost + " Client port = " + clientPort);

                // Read data from the client
                InputStream clientIn = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientIn));
                String msgFromClient = br.readLine();
                System.out.println("Message received from client = " + msgFromClient);

                // Send response to the client
                if(msgFromClient != null) {
                    OutputStream clientOut = client.getOutputStream();
                    PrintWriter pw = new PrintWriter(clientOut, true);
                    try {
                        if (msgFromClient.contains("*")) {
                            String[] parts = msgFromClient.split("\\*");
                            int num1 = Integer.parseInt(parts[0]);
                            int num2 = Integer.parseInt(parts[1]);
                            pw.println(msgFromClient + " är lika med " + (num1 * num2));
                        } else if (msgFromClient.contains("+")) {
                            String[] parts = msgFromClient.split("\\+");
                            int num1 = Integer.parseInt(parts[0]);
                            int num2 = Integer.parseInt(parts[1]);
                            pw.println(msgFromClient + " är lika med " + (num1 + num2));
                        } else if (msgFromClient.contains("-")) {
                            String[] parts = msgFromClient.split("-");
                            int num1 = Integer.parseInt(parts[0]);
                            int num2 = Integer.parseInt(parts[1]);
                            pw.println(msgFromClient + " är lika med " + (num1 - num2)); 
                        } else if (msgFromClient.contains("/")) {
                            String[] parts = msgFromClient.split("/");
                            int num1 = Integer.parseInt(parts[0]);
                            int num2 = Integer.parseInt(parts[1]);
                            if (num2 == 0) {
                                pw.println("Fel: Division med noll");
                            } 
                            pw.println(msgFromClient + " är lika med " + (num1 / num2));
                        } else {
                            pw.println("Ogiltigt uttryck"); 
                        }
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Felaktigt format på uttrycket"); 
                        }
                    }

                // Close sockets
                if(msgFromClient != null &&
                msgFromClient.equalsIgnoreCase("bye")) {
                    server.close();
                    client.close();
                    break;
                }
            } catch(IOException ie) {
                System.out.println("Ojzan, foonka inte");
            }
        }
    }
}