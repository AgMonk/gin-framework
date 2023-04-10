package entity;

import base.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 系统角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 15:32
 */
@Getter
@Setter
@TableName(value = SystemRole.TABLE_NAME, autoResultMap = true)
@Entity(name = SystemRole.TABLE_NAME)
@NoArgsConstructor
public class SystemRole extends BasePo {
    protected static final String TABLE_NAME = "t_system_entity_role";
    @Column(length = 50, nullable = false, unique = true)
    @Comment("名称")
    String name;
    @Column(length = 50, nullable = false, unique = true)
    @Comment("中文名称")
    String nameZh;
    @Column(length = 200)
    @Comment("描述")
    String description;
    @Column(length = 200)
    @Comment("备注")
    String remark;
    @Column
    @Comment("修改时间(UNIX秒)")
    Long timeUpdate;


}