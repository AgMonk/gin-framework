package com.gin.security.service.impl;


import com.gin.security.dao.SystemUserAvatarDao;
import com.gin.security.entity.SystemUserAvatar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gin.common.properties.FileProperties;
import com.gin.database.service.AttachmentServiceImpl;
import com.gin.security.service.SystemUserAvatarService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 16:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemUserAvatarServiceImpl extends AttachmentServiceImpl<SystemUserAvatarDao, SystemUserAvatar> implements SystemUserAvatarService {
    public SystemUserAvatarServiceImpl(FileProperties fileProperties) {
        super(fileProperties);
    }

    @Override
    public String attachPath() {
        return "/user/avatar";
    }
}