package entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.utils.WebUtils;
import com.gin.springboot3template.user.security.utils.MySecurityUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 系统操作日志
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 11:29
 */
@Getter
@Setter
@TableName(value = SystemOperationLog.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemOperationLog.TABLE_NAME)
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "mainClass,mainId,type,subClass,subId,timeCreate"),
        @Index(columnList = "mainClass,subClass,type"),
})
public class SystemOperationLog extends BaseOperationLog {
    public static final String TABLE_NAME = "t_system_entity_operation_log";

    public SystemOperationLog(@NotNull OperationType type) {
        this.type = type;
        this.userId = MySecurityUtils.currentUserDetails().getId();
        this.userIp = WebUtils.getRemoteHost();
    }
}