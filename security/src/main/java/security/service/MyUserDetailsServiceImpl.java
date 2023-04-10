package security.service;


import entity.SystemUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.bo.MyUserDetails;
import security.interfaze.AuthorityProvider;
import service.RolePermissionService;
import service.SystemUserService;
import utils.SpringContextUtils;

import java.util.Collection;


/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 15:19
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {

    private final SystemUserService systemUserService;
    private final RolePermissionService rolePermissionService;

    /**
     * 根据用户名查询用户的认证授权信息
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.UserDetails
     * @throws UsernameNotFoundException 异常
     * @since 2022/12/6 15:03
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final SystemUser systemUser = systemUserService.getByUsername(username);
        if (systemUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        final MyUserDetails userDetails = MyUserDetails.of(systemUser);

        //权限提供者提供的权限
        final Collection<AuthorityProvider> providers = SpringContextUtils.getContext().getBeansOfType(AuthorityProvider.class).values();
        providers.forEach(a -> userDetails.addAuthorities(a.getAuthorities(systemUser.getId())));

        return userDetails;
    }

    /**
     * 修改密码
     * @param user        用户
     * @param newPassword 新密码
     * @return UserDetails
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        final SystemUser systemUser = systemUserService.getByUsername(user.getUsername());
        systemUser.setPassword(newPassword);
        systemUserService.updateById(systemUser);
        return MyUserDetails.of(systemUser);
    }
}
