package base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import constant.FieldDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

/**
 * 基础字段
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 11:22
 */
@Getter
@Setter
@MappedSuperclass
@Schema(description = "基础字段")
public class BaseFields implements Serializable {
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Comment(FieldDescription.ID)
    @Schema(description = FieldDescription.ID)
    Long id;

    @Column(nullable = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Comment(FieldDescription.CREATE_TIME)
    @Schema(description = FieldDescription.CREATE_TIME)
    Long timeCreate;
}   
