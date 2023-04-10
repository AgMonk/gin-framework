package com.gin.database.dto.param;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * 带时间区间的分页查询参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/6 12:45
 */
@Getter
@Setter
@Validated
public abstract class AbstractTimePageParam extends BasePageParam {
    @JsonUnwrapped
    TimeRange range;
}
