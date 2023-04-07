package old.sys.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


/**
 * 基础分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:39
 */
@Getter
@Setter
@Validated
@Schema(description = "分页查询条件")
public abstract class BasePageParam {
    @Schema(description = "当前页,默认值:1", defaultValue = "1")
    @Min(value = 1L, message = "页码最小为1")
    int page = 1;

    @Schema(description = "每页条数,默认值:10,取值范围[10,50]", defaultValue = "10")
    @Min(value = 10L, message = "条数最小为1")
    @Max(value = 50L, message = "条数最大为50")
    int size = 10;

    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    public abstract void handleQueryWrapper(QueryWrapper<?> queryWrapper);

}   
