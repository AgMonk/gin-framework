package entity;

import base.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 角色持有的权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:06
 */
@Getter
@Setter
@TableName(value = RelationRolePermission.TABLE_NAME, autoResultMap = true)
@Entity(name = RelationRolePermission.TABLE_NAME)
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_role", columnNames = {"roleId", "permissionId"}),
})
public class RelationRolePermission extends BasePo {
    protected static final String TABLE_NAME = "t_system_relation_role_permission";
    @Column(nullable = false)
    @Comment("角色id")
    Long roleId;

    @Column(nullable = false)
    @Comment("权限id")
    Long permissionId;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationRolePermission that = (RelationRolePermission) o;
        return getRoleId().equals(that.getRoleId()) && getPermissionId().equals(that.getPermissionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getPermissionId());
    }
}