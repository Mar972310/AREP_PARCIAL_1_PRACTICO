package edu.arep.parcial1.HttpServerFachada;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.arep.parcial1.HttpServer.RequestHandler;

public class HttpServerFachada {
    private static final int PORT= 35000; 
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
                threadpool.submit(new RequestHandlerFachada(clientSocket));
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        serverSocket.close(); 
    }
    public static void main(String[] args) throws IOException {
        HttpServerFachada server = new HttpServerFachada();
        server.startServer();
  }
    
}
