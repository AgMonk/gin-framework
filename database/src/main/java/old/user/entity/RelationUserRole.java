package old.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gin.springboot3template.sys.base.BasePo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.Objects;

/**
 * 用户持有的角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:05
 */
@Getter
@Setter
@TableName(value = RelationUserRole.TABLE_NAME, autoResultMap = true)
@Entity(name = RelationUserRole.TABLE_NAME)
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_role", columnNames = {"userId", "roleId"}),
})
public class RelationUserRole extends BasePo {
    protected static final String TABLE_NAME = "t_system_relation_user_role";
    @Column(nullable = false)
    @Comment("用户id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long userId;
    @Column(nullable = false)
    @Comment("角色id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    Long roleId;

    @Column
    @Comment("修改时间(UNIX秒)")
    Long timeUpdate;

    @Column(nullable = false)
    @Comment("过期时间(UNIX秒)")
    Long timeExpire;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationUserRole that = (RelationUserRole) o;
        return getUserId().equals(that.getUserId()) && getRoleId().equals(that.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRoleId());
    }


}