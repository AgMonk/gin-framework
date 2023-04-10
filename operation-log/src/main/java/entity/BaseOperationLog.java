package entity;

import base.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import handler.TypeHandlerClass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import enums.OperationType;
import org.hibernate.annotations.Comment;

/**
 * 操作日志字段
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:07
 */
@Getter
@Setter
@MappedSuperclass
public class BaseOperationLog extends BasePo {
    @Comment("操作类型")
    @Schema(description = "操作类型")
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    OperationType type;
    @Comment("操作人ID")
    @Schema(description = "操作人ID")
    Long userId;
    @Comment("操作人IP")
    @Schema(description = "操作人IP")
    @Column(length = 40)
    String userIp;


    @Comment("主实体类型")
    @Schema(description = "主实体类型")
    @Column(nullable = false, length = 150)
    @TableField(typeHandler = TypeHandlerClass.class)
    Class<?> mainClass;
    @Comment("主实体ID")
    @Schema(description = "主实体ID")
    @Column(nullable = false)
    Long mainId;
    @Comment("副实体类型")
    @Schema(description = "副实体类型")
    @Column(length = 150)
    @TableField(typeHandler = TypeHandlerClass.class)
    Class<?> subClass;
    @Comment("副实体ID")
    @Schema(description = "副实体ID")
    @Column
    Long subId;
    @Comment("使用的策略类")
    @Schema(description = "使用的策略类")
    @Column(length = 100)
    @TableField(typeHandler = TypeHandlerClass.class)
    Class<?> strategyClass;
    @Comment("请求参数")
    @Schema(description = "请求参数")
    @Column(length = 2000)
    String requestParam;
    @Comment("返回结果")
    @Schema(description = "返回结果")
    @Column(length = 2000)
    String responseResult;

    @Comment("操作描述")
    @Schema(description = "操作描述")
    @Column(length = 10000)
    String description;

    @Comment("会话ID")
    @Schema(description = "会话ID")
    @Column(nullable = false, length = 36)
    String sessionId;

    @Comment("执行耗时(ms)")
    @Schema(description = "执行耗时(ms)")
    @Column(nullable = false)
    Long timeCost;

    /**
     * 统计用,非库表字段
     */
    @Transient
    @TableField(exist = false)
    @Schema(hidden = true)
    Integer count;


}
