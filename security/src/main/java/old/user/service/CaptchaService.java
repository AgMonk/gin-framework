package old.user.service;

import com.gin.springboot3template.sys.enums.CaptchaType;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import static com.gin.springboot3template.sys.bo.Constant.Security.COLON;
import static com.gin.springboot3template.sys.bo.Constant.Security.VERIFY_CODE_KEY;

/**
 * 验证码服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/28 09:18
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {
    public static final int TIMEOUT_MINUTES = 5;
    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final int LENGTH = 3;
    private final RedisTemplate<String, String> stringTemplate;

    /**
     * 创建验证码 并保存到redis,图片写入到 outputStream
     * @param key          保存的key
     * @param type         创建的验证码类型
     * @param outputStream outputStream
     */
    public final void create(@NotNull String key, CaptchaType type, OutputStream outputStream) {
        final Captcha captcha = buildCaptcha(type);
        //验证码答案
        final String text = captcha.text();
        //写入redis
        saveVerifyCode(key, text);
        captcha.out(outputStream);
    }

    /**
     * 创建验证码 并保存到redis,图片转换为base64返回
     * @param key  保存的key
     * @param type 创建的验证码类型
     * @return base64格式图片
     */
    public final String create(@NotNull String key, CaptchaType type) {
        final Captcha captcha = buildCaptcha(type);
        //验证码答案
        final String text = captcha.text();
        //写入redis
        saveVerifyCode(key, text);
        return captcha.toBase64();
    }

    /**
     * 校验验证码 ,校验成功后清除保存的验证码;校验失败报错
     */
    public final void validate(@NotNull String key, String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new BadCredentialsException("请输入验证码");
        }
        final String redisKey = getRedisKey(key);
        final Boolean hasKey = stringTemplate.hasKey(redisKey);
        if (hasKey == null || !hasKey) {
            throw new BadCredentialsException("验证码不存在或已过期,请先获取验证码");
        }
        final String text = stringTemplate.opsForValue().get(redisKey);
        if (value.equalsIgnoreCase(text)) {
            //验证码正确 删除redis中的验证码
            stringTemplate.delete(redisKey);
        } else {
            //验证码错误 报错
            throw new BadCredentialsException("验证码错误");
        }
    }

    private static Captcha buildCaptcha(CaptchaType type) {
        if (type == null) {
            return new ArithmeticCaptcha(WIDTH, HEIGHT, LENGTH);
        }
        final Class<? extends Captcha> clazz = type.getClazz();
        if (GifCaptcha.class.equals(clazz)) {
            return new GifCaptcha(WIDTH, HEIGHT, LENGTH);
        } else if (ArithmeticCaptcha.class.equals(clazz)) {
            return new ArithmeticCaptcha(WIDTH, HEIGHT, LENGTH);
        } else if (ChineseCaptcha.class.equals(clazz)) {
            return new ChineseCaptcha(WIDTH, HEIGHT, LENGTH);
        }
        return new SpecCaptcha(WIDTH, HEIGHT, LENGTH);
    }

    @NotNull
    private static String getRedisKey(String key) {
        return VERIFY_CODE_KEY + COLON + key;
    }

    private void saveVerifyCode(String key, String text) {
        stringTemplate.opsForValue().set(getRedisKey(key), text, TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }
}
