package com.gin.database.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonUnwrapped
    PageSize pageSize;
    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    public abstract void handleQueryWrapper(QueryWrapper<?> queryWrapper);
}
