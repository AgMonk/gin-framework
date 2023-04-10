package base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

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
public class BasePo extends BaseFields implements Serializable {
    public BasePo() {
        this.setTimeCreate(System.currentTimeMillis() / 1000);
    }
}
