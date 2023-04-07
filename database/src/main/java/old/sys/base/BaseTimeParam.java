package old.sys.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * 基础时间查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/6 12:43
 */
@Getter
@Setter
@Validated
@Schema(description = "基础时间查询参数")
public class BaseTimeParam {
    @Schema(description = "最晚时间")
    Long maxTime;
    @Schema(description = "最早时间")
    Long minTime;

    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        if (maxTime != null) {
            queryWrapper.lt("time_create", maxTime);
        }
        if (minTime != null) {
            queryWrapper.ge("time_create", minTime);
        }
    }

}
