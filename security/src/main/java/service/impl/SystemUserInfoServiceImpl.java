package service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dao.SystemUserInfoDao;
import entity.SystemUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.SystemUserInfoService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemUserInfoServiceImpl extends ServiceImpl<SystemUserInfoDao, SystemUserInfo> implements SystemUserInfoService {
}