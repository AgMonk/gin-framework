package com.gin.operationlog.service;

import entity.SystemOperationLogOld;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:11
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemOperationLogOldService extends OperationLogService<SystemOperationLogOld> {


}