package old.sys.enums;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.Getter;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/28 10:13
 */
@Getter
public enum CaptchaType {
    /**
     * 验证码类型
     */

    GIF(GifCaptcha.class),
    ARI(ArithmeticCaptcha.class),
    CHI(ChineseCaptcha.class),
    SPE(SpecCaptcha.class),
    ;
    final Class<? extends Captcha> clazz;

    CaptchaType(Class<? extends Captcha> clazz) {
        this.clazz = clazz;
    }

}
