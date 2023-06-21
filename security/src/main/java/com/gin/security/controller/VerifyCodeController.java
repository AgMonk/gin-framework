package com.gin.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.gin.security.enums.CaptchaType;
import com.gin.security.service.CaptchaService;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gin.spring.vo.response.Res;

import java.io.IOException;

/**
 * 验证码接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/7 09:50
 */
@Controller
@RequestMapping("/sys/verifyCode")
@RequiredArgsConstructor
@Tag(name = "验证码接口")
public class VerifyCodeController {
    private static final String DESCRIPTION = "获取验证码 <br/>" +
            "type 参数为指定生成的验证码类型, 具体效果可以尝试请求查看 ,为空时默认为 ARI; <br/>" +
            "一般来说,用于同一功能的验证码类型应保持不变 , 除非需要较高的安全性;不同功能可以使用不同类型";
    private final CaptchaService captchaService;

    @GetMapping("/base64")
    @Operation(summary = "Base64格式", description = DESCRIPTION)
    @ResponseBody
    public Res<String> base64(HttpSession httpSession, @RequestParam(defaultValue = "ARI") CaptchaType type) {
        return Res.of(captchaService.create(httpSession.getId(), type));
    }

    @GetMapping("/image")
    @Operation(summary = "图片格式", description = DESCRIPTION)
    public void image(
            HttpServletResponse response,
            HttpSession httpSession,
            @RequestParam(defaultValue = "ARI") CaptchaType type
    ) throws IOException {
        response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        captchaService.create(httpSession.getId(), type, response.getOutputStream());
    }

}
