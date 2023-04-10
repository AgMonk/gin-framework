package com.gin.security.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 删除角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 09:27
 */
@Getter
@Setter
@Schema(description = "删除角色表单")
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SystemRoleDelForm {
    @Schema(description = "角色id")
    @NotEmpty
    List<Long> roleId;

}   