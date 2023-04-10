package old.sys;

import com.gin.springboot3template.sys.utils.CollUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 15:33
 */
public class Test {


    public static void main(String[] args) throws IOException, InterruptedException, IllegalAccessException {
        List<Integer> keys = List.of(1, 6, 4, 5, 3);
        List<Integer> values = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        System.out.println(CollUtils.pick(keys, values, Objects::equals));
    }
}
