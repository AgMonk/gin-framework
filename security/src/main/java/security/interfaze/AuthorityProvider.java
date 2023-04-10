package security.interfaze;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * 权限提供者
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 13:49
 */
public interface AuthorityProvider {
    /**
     * 根据用户id查询用户持有的权限（角色+权限）
     * @param userId 用户id
     * @return 用户持有的权限
     */
    Set<GrantedAuthority> getAuthorities(long userId);
}
