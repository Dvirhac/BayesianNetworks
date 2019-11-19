
import java.io.*;
import java.util.regex.Pattern;

public class MyFile {
    /**
     *
     * @param path
     * @return String Array contains the text as invidual blocks of the information
     * @throws IOException
     */
    public static String[] getData(String path) throws IOException {
        FileInputStream fstream = new FileInputStream(path);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        StringBuilder sb = new StringBuilder();
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            sb.append(strLine).append("\n");
        }

        String fileAsString = sb.toString();
        Pattern p = Pattern.compile("\\n[\\n]+");     /*if your text file has \r\n as the newline character then use Pattern p = Pattern.compile("\\r\\n[\\r\\n]+");*/
        String[] lines = p.split(fileAsString);
        fstream.close();

        return lines;
    }
}
