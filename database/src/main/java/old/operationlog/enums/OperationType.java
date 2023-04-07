package old.operationlog.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * 操作类型
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 13:55
 */
@RequiredArgsConstructor
public enum OperationType {
    /**
     * 添加
     */
    ADD("添加"),
    /**
     * 删除
     */
    DEL("删除"),
    /**
     * 修改
     */
    UPDATE("修改"),
    /**
     * 查询
     */
    QUERY("查询"),
    LOGIN("登录"),
    LOGIN_FAILED("登录失败"),
    LOGOUT("登出"),
    DOWNLOAD("下载"),
    UPLOAD("上传"),
    BACKUP("备份"),
    RECOVER("还原"),
    ;
    final String name;

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, name());
    }

}
