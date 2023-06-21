package com.gin.database.vo.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gin.common.constant.Messages;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 13:48
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页数据响应对象")
public class ResPage<T> extends Res<List<T>> {
    @Schema(description = "页数")
    Long page;
    @Schema(description = "每页条数")
    Long size;
    @Schema(description = "总条数")
    Long total;
    @Schema(description = "总页数")
    Long totalPage;

    public static <T> ResPage<T> of(Page<T> page) {
        final ResPage<T> data = new ResPage<>();
        data.setPage(page.getCurrent());
        data.setSize(page.getSize());
        data.setTotal(page.getTotal());
        data.setTotalPage(page.getPages());
        data.setMessage(CollectionUtils.isEmpty(page.getRecords()) ? Messages.DATA_NOT_FOUND : "ok");
        data.setData(page.getRecords());
        return data;
    }

    public static <T, R> ResPage<R> of(Page<T> page, Function<T, R> func) {
        final ResPage<R> data = new ResPage<>();
        data.setPage(page.getCurrent());
        data.setSize(page.getSize());
        data.setTotal(page.getTotal());
        data.setTotalPage(page.getPages());
        data.setMessage(CollectionUtils.isEmpty(page.getRecords()) ? Messages.DATA_NOT_FOUND : "ok");
        data.setData(page.getRecords().stream().map(func).toList());
        return data;
    }

}
