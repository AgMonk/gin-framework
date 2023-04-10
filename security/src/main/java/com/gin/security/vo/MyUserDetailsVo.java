package com.gin.security.vo;


import com.gin.database.base.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * 用户认证、权限信息
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:36
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "用户认证/授权信息")
public class MyUserDetailsVo extends BaseVo {
    @Schema(description = "账号未过期")
    private boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    private boolean accountNonLocked;
    @Schema(description = "权限")
    private Set<GrantedAuthority> authorities;
    @Schema(description = "密码未过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    private boolean enabled;
    @Schema(description = "用户名")
    private String username;

    /**
     * 获取当前用户认证/授权信息
     * @return 用户认证/授权信息
     */
    public static MyUserDetailsVo of() {
        return of(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 从 authentication 中获取用户认证/授权信息
     * @param authentication authentication
     * @return 用户认证/授权信息
     */
    public static MyUserDetailsVo of(Authentication authentication) {
        final MyUserDetailsVo details = new MyUserDetailsVo();
        details.with(authentication.getPrincipal());
        return details;
    }

    public MyUserDetailsVo with(Object obj) {
        BeanUtils.copyProperties(obj, this);
        return this;
    }
}
