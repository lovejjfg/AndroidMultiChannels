


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BuildParams {

    public String[] chanelNames;
    public String apkPath;
    public static String CHANNEL = "channel_" ; 

    public BuildParams() {
        try {
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("data.properties");
            properties.load(inputStream);
            String chanelNamess = properties.getProperty("CHANNEL_NAME", "");
            apkPath= properties.getProperty("APK_PATH", "");
           	chanelNames =  chanelNamess.split(";");
           	CHANNEL = properties.getProperty("CHANNEL", "channel_");
            inputStream.close();
            properties.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
