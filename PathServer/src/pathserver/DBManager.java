package pathserver;

import java.sql.*;

public class DBManager {

    private final String url = "jdbc:mysql://10.10.15.1:3306/mapping_test";
    private Connection con;
    private boolean Connected;

    DBManager(){
        Connected = false;
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void Connect(){
        //
        try{

            con = DriverManager.getConnection(url, "root", "Track2eagle");
            Connected = true;
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public void Disconnect(){
        try{
            con.close();
            Connected = false;
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public boolean isConnected(){
        return Connected;
    }
}
