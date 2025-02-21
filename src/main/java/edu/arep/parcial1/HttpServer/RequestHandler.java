package edu.arep.parcial1.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class RequestHandler implements Runnable{
    private Socket clientSocket;
    @Override
    public void run() {
        try {
            requestHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }

    public RequestHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void requestHandler() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        PrintWriter out = new PrintWriter(
                                    clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        String firstLine = in.readLine();
        boolean isFirtsLine = true;
        String url = firstLine.split(" ")[1];
        String method = firstLine.split(" ")[0];
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Recibí: " + inputLine);
            if (!in.ready()) {break; }
        }
        outputLine = response(url);
        out.println(outputLine);
        out.close(); 
        in.close(); 
        clientSocket.close(); 
    }

    private String response(String query) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        String response = ReflexCalculator.calculator(query);
        String outputline = "HTTP/1.1. 200 OK \r\n"+
                "ContentType: text/json\r\n"+
                "\r\n"+
                "{\"name\":\""+ response+"\"}";
        return outputline;
    }
    
    
}
