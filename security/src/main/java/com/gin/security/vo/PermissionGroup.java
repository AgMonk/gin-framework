package com.gin.security.vo;


import com.gin.security.entity.SystemPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/21 17:13
 */
@Getter
@Setter
@Schema(description = "按组名分组的权限")
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroup {
    @Schema(description = "组名")
    String groupName;
    @Schema(description = "权限")
    List<SystemPermission> permissions;
}   
