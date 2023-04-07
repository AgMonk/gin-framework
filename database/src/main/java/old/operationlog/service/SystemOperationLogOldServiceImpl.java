package old.operationlog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.operationlog.dao.SystemOperationLogOldDao;
import com.gin.springboot3template.operationlog.entity.SystemOperationLogOld;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemOperationLogOldServiceImpl extends ServiceImpl<SystemOperationLogOldDao, SystemOperationLogOld> implements SystemOperationLogOldService {
}