package old.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.service.MyService;
import com.gin.springboot3template.user.dto.form.RegForm;
import com.gin.springboot3template.user.entity.SystemUser;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:45
 */
public interface SystemUserService extends MyService<SystemUser> {
    /**
     * 修改密码
     * @param userId  需要修改密码的用户id
     * @param oldPass 旧密码
     * @param newPass 新密码
     */
    void changePwd(Long userId, String oldPass, String newPass);

    /**
     * 修改密码
     * @param userId  用户id
     * @param newPass 新密码
     */
    void changePwd(Long userId, String newPass);

    /**
     * 注册
     * @param regForm 注册表单
     * @return 用户
     */
    SystemUser reg(RegForm regForm);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    default SystemUser getByUsername(String username) {
        final QueryWrapper<SystemUser> qw = new QueryWrapper<>();
        qw.eq("username", username);
        return getOne(qw);
    }

    /**
     * 根据用户名查询用户,如果不存在则注册一个
     * @param regForm 注册表单
     * @return 用户
     */
    default SystemUser getByUsernameOrReg(RegForm regForm) {
        final SystemUser user = getByUsername(regForm.getUsername());
        return user != null ? user : reg(regForm);
    }
}
