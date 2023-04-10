package service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dao.SystemPermissionDao;
import entity.SystemPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.SystemPermissionService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemPermissionServiceImpl extends ServiceImpl<SystemPermissionDao, SystemPermission> implements SystemPermissionService {
}