package old.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.user.dao.RelationRolePermissionDao;
import com.gin.springboot3template.user.entity.RelationRolePermission;
import com.gin.springboot3template.user.service.RelationRolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RelationRolePermissionServiceImpl extends ServiceImpl<RelationRolePermissionDao, RelationRolePermission> implements RelationRolePermissionService {
}