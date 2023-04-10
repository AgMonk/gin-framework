package com.gin.database.dto.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询需要的页码和每页条数(建议：放在其他类中时,使用注解 {@link com.fasterxml.jackson.annotation.JsonUnwrapped } 直接使用时使用 {@link  org.springdoc.core.annotations.ParameterObject} 注解)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 10:23
 */
@Getter
@Setter
public class PageSize {
    @Schema(description = "当前页,默认值:1", defaultValue = "1")
    @Min(value = 1L, message = "页码最小为1")
    int page = 1;

    @Schema(description = "每页条数,默认值:10,取值范围[10,50]", defaultValue = "10")
    @Min(value = 10L, message = "条数最小为1")
    @Max(value = 50L, message = "条数最大为50")
    int size = 10;

    public <T> Page<T> buildPage() {
        return new Page<>(page, size);
    }
}
