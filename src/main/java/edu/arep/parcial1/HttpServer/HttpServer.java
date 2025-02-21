package edu.arep.parcial1.HttpServer;

import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class HttpServer {
    private static final int PORT= 36000; 
    private ExecutorService threadpool;
    private ServerSocket serverSocket = null;
    private Socket clientSocket;
    private boolean running=true;


    public void startServer() throws IOException{
        try { 
            serverSocket = new ServerSocket(PORT);
            threadpool = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            System.err.println("Could not listen on port: "+String.valueOf(PORT));
            System.exit(1);
        }
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                threadpool.submit(new RequestHandler(clientSocket));
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        serverSocket.close(); 
    }
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();
        server.startServer();
  }
}