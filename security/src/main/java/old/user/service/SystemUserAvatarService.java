package old.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.service.AttachmentService;
import com.gin.springboot3template.user.entity.SystemUserAvatar;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 16:55
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemUserAvatarService extends AttachmentService<SystemUserAvatar> {
    /**
     * 允许的文件ContentType
     * @return ContentType 列表
     */
    @Override
    default List<String> acceptContentType() {
        return List.of(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    }

    /**
     * 删除指定用户的头像
     * @param userId 用户id
     * @return 被删除的头像
     */
    default SystemUserAvatar deleteByUserId(long userId) {
        final SystemUserAvatar avatar = getByUserId(userId);
        if (avatar != null) {
            return deleteEntities(Collections.singletonList(avatar)).get(0);
        }
        return null;
    }

    /**
     * 根据用户id 查询头像
     * @param userId 用户ID
     * @return 头像
     */
    default SystemUserAvatar getByUserId(long userId) {
        final QueryWrapper<SystemUserAvatar> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return getOne(qw);
    }

    /**
     * 根据用户id 查询头像
     * @param userIds 用户ID
     * @return 头像
     */
    default List<SystemUserAvatar> listByUserId(Collection<Long> userIds) {
        final QueryWrapper<SystemUserAvatar> qw = new QueryWrapper<>();
        qw.in("user_id", userIds);
        return list(qw);
    }

    /**
     * 使用用户Id上传头像
     * @param file   文件
     * @param userId 用户id
     * @return 上传的头像
     * @throws IOException 异常
     */
    default SystemUserAvatar uploadWithUserId(MultipartFile file, long userId) throws IOException {
        final SystemUserAvatar avatar = new SystemUserAvatar();
        avatar.setUserId(userId);
        avatar.setOwnerId(userId);
        avatar.setUploaderId(userId);
        return upload(file, avatar);
    }
}