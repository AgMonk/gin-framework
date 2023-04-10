package Constant;

/**
 * 安全相关
 * @author bx002
 */
public class Security {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    public static final String COLON = ":";
    public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    public static final String LOGOUT_URI = "/sys/user/logout";
    /**
     * 密码最大位数
     */
    public static final int PASSWORD_MAX_LENGTH = 20;
    /**
     * 密码最小位数
     */
    public static final int PASSWORD_MIN_LENGTH = 6;
    /**
     * preAuthority注解的内容,含义为:需要访问当前接口uri的权限,或者是admin角色
     */
    public static final String PRE_AUTHORITY_URI_OR_ADMIN = "hasAuthority(#request.requestURI) or hasRole('admin')";
    public static final String REMEMBER_ME_KEY = "rememberMe";
    public static final String VERIFY_CODE_KEY = "vc";

    public static final String ACCESS_DENIED = "禁止访问";
    public static final String FORBIDDEN_CONFIG_ADMIN = "不能分配/取消分配 admin 角色";
    public static final String NOT_CONFIG_ADMIN = "不能对持有 admin 角色 的用户进行操作";
}