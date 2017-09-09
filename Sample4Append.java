import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import com.machbase.jdbc.*;

public class Sample4Append
{
    protected static final String sTableName = "sample_table";
    protected static final int sErrorCheckCount = 100;

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
        MachStatement stmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String filename = "data.txt";

        try
        {
            conn = connect();
            if( conn != null )
            {
            	System.out.println("machbase JDBC connected.");

                stmt = (MachStatement)conn.createStatement();

                ResultSet rs = stmt.executeAppendOpen(sTableName, sErrorCheckCount);
                ResultSetMetaData rsmd = rs.getMetaData();

                System.out.println("append open ok");

                MachAppendCallback cb = new MachAppendCallback() {
                        @Override
                        public void onAppendError(long aErrNo, String aErrMsg, String aRowMsg) {
                            System.out.format("Append Error : [%05d - %s]\n%s\n", aErrNo, aErrMsg, aRowMsg);
                        }
                    };

                stmt.executeSetAppendErrorCallback(cb);

                System.out.println("append data start");
                BufferedReader in = new BufferedReader(new FileReader(filename));
                String buf = null;
                int cnt = 0;
                long dt;

                long startTime = System.nanoTime();

                while( (buf = in.readLine()) != null )
                {
                    ArrayList<Object> sBuf = new ArrayList<Object>();
                    StringTokenizer st = new StringTokenizer(buf,",");
                    for(int i=0; st.hasMoreTokens() ;i++ )
                    {
                        switch(i){
                            case 7://binary case
                                sBuf.add(new ByteArrayInputStream(st.nextToken().getBytes())); break;
                            case 10://datetime case
                                java.util.Date day = sdf.parse(st.nextToken());
                                cal.setTime(day);
                                dt = cal.getTimeInMillis()*1000000; //make nanotime
                                sBuf.add(dt);
                                break;
                            default:
                                sBuf.add(st.nextToken()); break;
                        }
                    }

                    if( stmt.executeAppendData(rsmd, sBuf) != 1 )
                    {
                        System.err.println("Error : AppendData error");
                        break;
                    }

                    if( (cnt++%10000) == 0 )
                    {
                        System.out.print(".");
                    }
                    sBuf = null;

                }
                System.out.println("\nappend data end");

                long endTime = System.nanoTime();

                stmt.executeAppendClose();

                System.out.println("append close ok");

                System.out.println("Append Result : success = "+stmt.getAppendSuccessCount()+", failure = "+stmt.getAppendFailureCount());

                System.out.println("timegap " + ((endTime - startTime)/1000) + " in microseconds, " + cnt + " records" );

                try {
                    BigDecimal records = new BigDecimal( cnt );
                    BigDecimal gap = new BigDecimal( (double)(endTime - startTime)/1000000000 );
                    BigDecimal rps = records.divide(gap, 2, BigDecimal.ROUND_UP );

                    System.out.println( rps + " records/second" );
                } catch(ArithmeticException ae) {
                    System.out.println( cnt + " records/second");
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
