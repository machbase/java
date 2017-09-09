import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import com.machbase.jdbc.*;

public class Sample3PrepareStmt
{
    public static Connection connect()
    {
        Connection conn = null;
        try
        {
            String sURL = "jdbc:machbase://localhost:5656/mhdb";

            Properties sProps = new Properties();
            sProps.put("user", "sys");
            sProps.put("password", "manager");

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
        Statement stmt = null;
        MachPreparedStatement preStmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

        try
        {
            conn = connect();
            if( conn != null )
            {
            	System.out.println("machbase JDBC connected.");

                stmt = conn.createStatement();

                preStmt = (MachPreparedStatement)conn.prepareStatement("INSERT INTO SAMPLE_TABLE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                String ipStr = null;
                String dateStr = null;
                for(int i=1; i<10; i++)
                {
                    ipStr = String.format("172.16.0.%d",i);
                    dateStr = String.format("2014-08-09 12:23:34 %03d", i);
                    byte[] bin = new byte[20];
                    for(int j=0;j<20;j++){
                        bin[j]=(byte)(Math.random()*255);
                    }
                    java.util.Date day = sdf.parse(dateStr);
                    java.sql.Date sqlDate = new java.sql.Date(day.getTime());

                    preStmt.setShort(1, (i-5) * 3276 );
                    preStmt.setInt(2, (i-5) * 214748364 );
                    preStmt.setLong(3, (i-5) * 922337203685477580L );
                    preStmt.setFloat(4, 1.23456789101112131415*Math.pow(10,i));
                    preStmt.setDouble(5, 1.23456789101112131415*Math.pow(10,i*10));
                    preStmt.setString(6, String.format("varchar-%d",i));//varchar
                    preStmt.setString(7, String.format("text-%d",i));//text
                    preStmt.setBytes(8, bin);//binary
                    preStmt.setIpv4(9, ipStr);//ipv4
                    preStmt.setIpv6(10, "::"+ipStr);//ipv6
                    preStmt.setDate(11, sqlDate);//datetime
                    preStmt.executeUpdate();

                    System.out.println( i+" record inserted.");
                }


                //date type format : YYYY-MM-DD HH24:MI:SS mmm:uuu:nnnn
                String query = "SELECT d1, d2, d3, f1, f2, name, text, bin, to_hex(bin), v4, v6, to_char(dt,'YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn') as dt from SAMPLE_TABLE";
                ResultSet rs = stmt.executeQuery(query);
                while( rs.next () )
                {
                    short d1 = rs.getShort("d1");
                    int d2 = rs.getInt("d2");
                    long d3 = rs.getLong("d3");
                    float f1 = rs.getFloat("f1");
                    double f2 = rs.getDouble("f2");
                    String name = rs.getString("name");
                    String text = rs.getString("text");
                    String bin = rs.getString("bin");
                    String hexbin = rs.getString("to_hex(bin)");
                    String v4 = rs.getString("v4");
                    String v6 = rs.getString("v6");
                    String dt = rs.getString("dt");

                    System.out.print("d1: " + d1);
                    System.out.print(", d2: " + d2);
                    System.out.print(", d3: " + d3);
                    System.out.print(", f1: " + f1);
                    System.out.print(", f2: " + f2);
                    System.out.print(", name: " + name);
                    System.out.print(", text: " + text);
                    System.out.print(", bin: " + bin);
                    System.out.print(", hexbin: "+hexbin);
                    System.out.print(", v4: " + v4);
                    System.out.print(", v6: " + v6);
                    System.out.println(", dt: " + dt);

                }
                rs.close();
            }
        }
        catch( SQLException se )
        {
            System.err.println("SQLException : " + se.getMessage());
        }
        catch( Exception e )
        {
            System.err.println("Exception : " + e.getMessage());
        }
        finally
        {
            if( stmt != null )
            {
                stmt.close();
                stmt = null;
            }
            if( conn != null )
            {
                conn.close();
                conn = null;
            }
        }
    }
}
