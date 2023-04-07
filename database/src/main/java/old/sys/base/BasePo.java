package old.sys.base;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

/**
 * 基础持久化对象
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:18
 */
@Getter
@Setter
@MappedSuperclass
@Schema(description = "基础持久化对象")
public class BasePo implements Serializable {
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Comment("ID")
    @Schema(description = "ID")
    Long id;

    @Column(nullable = false)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @Comment("记录创建时间(UNIX秒)")
    @Schema(description = "记录创建时间(UNIX秒)")
    Long timeCreate;

    public BasePo() {
        this.timeCreate = System.currentTimeMillis() / 1000;
    }
}
