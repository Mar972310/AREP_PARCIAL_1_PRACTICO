package edu.arep.parcial1.HttpServerFachada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class RequestHandlerFachada implements Runnable {
    private Socket clientSocket;
    private String route = "http://localhost:36000";
    @Override
    public void run() {
        try {
            requestHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    public RequestHandlerFachada(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void requestHandler() throws IOException{
        PrintWriter out = new PrintWriter(
                                    clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        outputLine = "";
        String firstLine = in.readLine();
        boolean isFirtsLine = true;
        String url = firstLine.split(" ")[1];
        String method = firstLine.split(" ")[0];
        URL obj = new URL(route+url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        System.out.println(url);
        if(url.startsWith("/calculadora")){
            outputLine = responseClient();
        }else if (url.startsWith("/computar")){
            while ((inputLine = in.readLine()) != null) {
                if(isFirtsLine){
                    isFirtsLine = false;
                }
                String[] properties = inputLine.split(": ",2);
                if(properties.length == 2){
                    con.setRequestProperty(properties[0], properties[1]);
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {break; }
            }
            outputLine = response(con);
        }
        out.println(outputLine);
        out.close(); 
        in.close(); 
        clientSocket.close(); 
    }

    private String response(HttpURLConnection con) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String outputline = "HTTP/1.1. 200 OK \r\n"+
                "ContentType: text/html\r\n"+
                "\r\n"+
                response.toString();
        return outputline;
    }

    private String responseClient(){
        String outputline = "HTTP/1.1 200 OK\r\n"
        + "Content-Type: text/html\r\n"
         + "\r\n"
         + "<!DOCTYPE html>\n"
         + "<html>\n"
         + "<head>\n"
         + "<meta charset=\"UTF-8\">\n"
         + "<title>Title of the document</title>\n"
         + "</head>\n"
         + "<body>\n"
         +"<form action=\"/hellopost\">\r\n" + //
            "<label for=\"postname\">Name:</label><br>\r\n" + //
            "<input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\r\n" + //
            "<input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\r\n" + //
            "</form>\r\n" + //
            " \r\n" + //
            " <div id=\"postrespmsg\"></div>\r\n" + //
            " \r\n" + //
            " <script>\r\n" + //
            " function loadPostMsg(name){\r\n" + //
            " let url = \"/hellopost?name=\" + name.value;\r\n" + //
            "\r\n" + //
            " fetch (url, {method: 'POST'})\r\n" + //
            " .then(x => x.text())\r\n" + //
            " .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\r\n" + //
            " }\r\n" + //
            "</script>"
         + "</body>\n"
         + "</html>\n";
        return outputline;
    }
}
