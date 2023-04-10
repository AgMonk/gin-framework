package com.gin.operationlog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
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
}