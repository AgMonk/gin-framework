package com.gin.database.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 基础视图对象
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:18
 */
@Getter
@Setter
@Schema(description = "基础视图对象")
@NoArgsConstructor
public class BaseVo extends BaseFields {
}
