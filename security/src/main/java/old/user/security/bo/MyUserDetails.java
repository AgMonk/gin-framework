package old.user.security.bo;

import base.BaseBo;
import constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户认证、权限信息(在系统内使用,不作为返回对象)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:36
 */
@Getter
@Setter
@NoArgsConstructor
public class MyUserDetails extends BaseBo implements UserDetails {

    @Schema(description = "账号未过期")
    private boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    private boolean accountNonLocked;
    @Schema(description = "权限")
    private Set<GrantedAuthority> authorities = new HashSet<>();
    @Schema(description = "密码未过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    private boolean enabled;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "用户名")
    private String username;

    /**
     * 获取当前用户认证/授权信息
     * @return 用户认证/授权信息
     */
    public static MyUserDetails of() {
        return of(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 从 authentication 中获取用户认证/授权信息
     * @param userDetails userDetails
     * @return 用户认证/授权信息
     */
    public static MyUserDetails of(Object userDetails) {
        final MyUserDetails details = new MyUserDetails();
        return details.with(userDetails);
    }

    public void addAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities.addAll(authorities);
    }

    public void addAuthority(GrantedAuthority authority) {
        this.authorities.add(authority);
    }

    public void addPermission(String permission) {
        addAuthority(new SimpleGrantedAuthority(permission));
    }

    public void addRole(String role) {
        addPermission(Constant.Security.DEFAULT_ROLE_PREFIX + role);
    }

    /**
     * @param authority 权限
     * @return 是否持有指定权限
     */
    public boolean hasAuthority(String authority) {
        for (GrantedAuthority grantedAuthority : this.authorities) {
            if (grantedAuthority.getAuthority().equalsIgnoreCase(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String role) {
        return hasAuthority(Constant.Security.DEFAULT_ROLE_PREFIX + role);
    }

    public MyUserDetails with(Object userDetails) {
        BeanUtils.copyProperties(userDetails, this);
        return this;
    }

}
