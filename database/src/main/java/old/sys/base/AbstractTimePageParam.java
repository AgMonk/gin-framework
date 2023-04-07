package old.sys.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/6 12:45
 */
@Getter
@Setter
@Validated
public abstract class AbstractTimePageParam extends BasePageParam {
    @Schema(description = "最晚时间")
    Long maxTime;
    @Schema(description = "最早时间")
    Long minTime;
}   
