package pathserver;

import java.io.*;
import java.net.*;

public class PathServer{

    public static final int maxConn = 20;

    public static void main(String[] args) {

        try{
            ServerSocket listener = new ServerSocket(1024);
            Socket server;
            int i = 0;

            while(i < maxConn){
                ConnThread connection;

                server = listener.accept();

                connection = new ConnThread(server);
                Thread t = new Thread(connection);
                t.start();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
}
