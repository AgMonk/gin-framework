package old.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Objects;

/**
 * 系统权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 15:33
 */
@Getter
@Setter
@TableName(value = SystemPermission.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemPermission.TABLE_NAME)
@NoArgsConstructor
@Schema(description = "接口权限")
public class SystemPermission extends BasePo {
    public static final String TABLE_NAME = "t_system_entity_permission";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("路径")
    @Schema(description = "路径")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    String path;
    @Column(length = 50)
    @Comment("分组")
    @Schema(description = "分组")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    String groupName;
    @Column(length = 50)
    @Comment("摘要")
    @Schema(description = "摘要")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    String summary;
    @Column(length = 100)
    @Comment("描述")
    @Schema(description = "描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    String description;

    @Column(length = 100)
    @Comment("权限检查")
    @Schema(description = "权限检查")
    String preAuthorize;

    public SystemPermission(String path, Tag tag, Operation operation, PreAuthorize preAuthorize) {
        super();
        final String authorize = preAuthorize.value();

        this.path = path;
        this.preAuthorize = authorize;


        if (operation != null) {
            this.summary = "".equals(operation.summary()) ? null : operation.summary();
            this.description = "".equals(operation.description()) ? null : operation.description();
        }
        if (tag != null) {
            this.groupName = "".equals(tag.name()) ? null : tag.name();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemPermission that = (SystemPermission) o;
        return getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

}