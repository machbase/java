import java.io.*;

public class MakeData{
    final static int MAX_DATA_COUNT=100000;

    public static void main(String[] args){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt",false));
            for(int i=1; i<=MAX_DATA_COUNT; i++)
            {
                writer.write((i%32768)+","+(i+i)+","+((long)(i+i+i)*10000)+","+((float)(i+2)/(i+i+i)*10000)+","+((double)(i+1)/(i+i+i)*10000)+",char-"+i+",text log-"+i+",binary image-"+i+",192.168.9."+(i%256)+",2001:0DB8:0000:0000:0000:0000:1428:"+(i%8999+1000)+",2015-05-18 15:26:"+(i%40+10)+"\n");
            }
            writer.close();
        }
        catch(Exception ex){
        }
    }
}