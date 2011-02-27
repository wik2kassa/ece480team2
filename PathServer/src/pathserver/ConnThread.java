package pathserver;

import java.io.*;
import java.net.*;

public class ConnThread implements Runnable{
    private Socket server;
    private String line,input;
    private DBManager db;
    private DataInputStream in;
    private PrintStream out;


    ConnThread(Socket Server){
        this.server = Server;
        db = new DBManager();
    }

    public void run() {
        input="";
        try{
            in = new DataInputStream(server.getInputStream());
            out = new PrintStream(server.getOutputStream());

            while((line = in.readLine()) != null && !line.equals("quit")) {
                input=input + line;
                out.println("I got:" + line);
                String args [] = line.split(" ");
                Process(args);
            }

            System.out.println("Exiting ...\n");
            out.println("Exiting\n");

            server.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public void Process(String [] l){
        if(l[0].equals("start")){

        }
        if(l[0].equals("end")){

        }
        if(l[0].equals("connection")){

        }
        if(l[0].equals("test")){
            db.Connect();
            out.println(db.isConnected());
            db.Disconnect();
        }
    }
}
