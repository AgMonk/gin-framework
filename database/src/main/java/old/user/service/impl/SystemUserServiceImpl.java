package old.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.user.dao.SystemUserDao;
import com.gin.springboot3template.user.dto.form.RegForm;
import com.gin.springboot3template.user.entity.SystemUser;
import com.gin.springboot3template.user.entity.SystemUserInfo;
import com.gin.springboot3template.user.service.SystemUserInfoService;
import com.gin.springboot3template.user.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserDao, SystemUser> implements SystemUserService {
    private final PasswordEncoder passwordEncoder;
    private final SystemUserInfoService systemUserInfoService;

    @Override
    public void changePwd(Long userId, String oldPass, String newPass) {
        if (oldPass.equalsIgnoreCase(newPass)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "新旧密码不能一致");
        }
        if (!passwordEncoder.matches(oldPass, getById(userId).getPassword())) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "旧密码错误");
        }
        changePwd(userId, newPass);
    }

    @Override
    public void changePwd(Long userId, String newPass) {
        final SystemUser user = new SystemUser();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(newPass));
        updateById(user);
    }

    @Override
    public SystemUser reg(RegForm regForm) {
        if (getByUsername(regForm.getUsername()) != null) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "用户名已存在");
        }
        //注册用户
        final SystemUser user = new SystemUser();
        user.setUsername(regForm.getUsername());
        user.setPassword(passwordEncoder.encode(regForm.getPassword()));
        save(user);

        //写入个人信息
        final SystemUserInfo info = new SystemUserInfo();
        BeanUtils.copyProperties(regForm, info);
        info.setUserId(user.getId());
        systemUserInfoService.save(info);

        return user;
    }
}
