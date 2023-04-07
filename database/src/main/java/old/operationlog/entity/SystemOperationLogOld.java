package old.operationlog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 系统操作日志(旧)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:09
 */
@Getter
@Setter
@TableName(value = SystemOperationLogOld.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemOperationLogOld.TABLE_NAME)
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "mainClass,mainId,subClass,subId,timeCreate"),
})
public class SystemOperationLogOld extends BaseOperationLog {
    public static final String TABLE_NAME = "t_system_entity_operation_log_old";


    public SystemOperationLogOld(SystemOperationLog systemOperationLog) {
        BeanUtils.copyProperties(systemOperationLog, this);
        this.setId(null);
    }
}
