package dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 时间范围参数 (建议：放在其他类中时,使用注解 {@link com.fasterxml.jackson.annotation.JsonUnwrapped } 直接使用时使用 {@link  org.springdoc.core.annotations.ParameterObject} 注解)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 10:19
 */
@Getter
@Setter
public class TimeRange {
    @Schema(description = "最晚时间")
    Long maxTime;
    @Schema(description = "最早时间")
    Long minTime;

    /**
     * 添加查询条件
     * @param queryWrapper qw
     * @param column       列名
     */
    public void handleQueryWrapper(@NotNull QueryWrapper<?> queryWrapper, @NotNull String column) {
        if (maxTime != null) {
            queryWrapper.lt(column, maxTime);
        }
        if (minTime != null) {
            queryWrapper.ge(column, minTime);
        }
    }
}   
