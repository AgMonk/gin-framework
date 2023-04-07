package old.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.user.dao.RelationUserRoleDao;
import com.gin.springboot3template.user.entity.RelationUserRole;
import com.gin.springboot3template.user.service.RelationUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RelationUserRoleServiceImpl extends ServiceImpl<RelationUserRoleDao, RelationUserRole> implements RelationUserRoleService {
}