import java.util.*;
import java.sql.*;
import com.machbase.jdbc.*;

public class Sample1Connect
{
    public static Connection connect() 
	{
        Connection conn = null;
        try 
		{
            String sURL = "jdbc:machbase://127.0.0.1:5656/mhdb";

            Properties sProps = new Properties();
            sProps.put("user", "SYS");
            sProps.put("password", "MANAGER");

            Class.forName("com.machbase.jdbc.driver");
            conn = DriverManager.getConnection(sURL, sProps);
        } 
		catch ( ClassNotFoundException ex ) 
		{
            System.err.println("Exception : unable to load machbase jdbc driver class");
		}
		catch ( Exception e ) 
		{
            System.err.println("Exception : " + e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) throws Exception 
	{
        Connection conn = null;

        try
        {
            conn = connect();
            if( conn != null )
            {
            	System.out.println("machbase JDBC connected.");
            }
        }
        catch( Exception e )
        {
            System.err.println("Exception : " + e.getMessage());
        }
        finally
        {
            if( conn != null )
            {
                conn.close();
                conn = null;
            }
        }
    }
}
