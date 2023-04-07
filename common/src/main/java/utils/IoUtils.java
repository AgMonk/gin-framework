package utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * IO工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 12:34
 */
public class IoUtils {

    public static void readLine(BufferedReader reader, Handler handler) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            handler.handle(line);
        }
        reader.close();
    }

    public interface Handler {
        void handle(String line);
    }
}
