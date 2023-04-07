package old.databsebackup.config;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据源连接参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 17:25
 */
@Getter
@Setter
public class DatabaseConConfig {
    public static final Pattern URL_PATTERN = Pattern.compile("^jdbc:mysql://(.+?):(\\d+?)/(.+?)\\?(.+)$");

    /**
     * 数据库名称
     */
    String database;
    /**
     * 服务器ip / 域名
     */
    String host;
    HashMap<String, String> params = new HashMap<>();
    Integer port;

    public DatabaseConConfig(String url) {
        final Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            this.host = matcher.group(1);
            this.port = Integer.valueOf(matcher.group(2));
            this.database = matcher.group(3);

            final String paramString = matcher.group(4);
            for (String pair : paramString.split("&")) {
                final String[] p = pair.split("=");
                params.put(p[0], p[1]);
            }
        }
    }
}
