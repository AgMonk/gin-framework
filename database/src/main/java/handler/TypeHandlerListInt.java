package handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.CollectionUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列表
 * @author bx002
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class TypeHandlerListInt extends BaseTypeHandler<List<Integer>> {

    public static final String DELIMITER = ",";

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String data = resultSet.getString(s);
        return isEmpty(data) ? new ArrayList<>() : Arrays.stream(data.split(DELIMITER)).map(Integer::parseInt).toList();
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String data = resultSet.getString(i);
        return isEmpty(data) ? new ArrayList<>() : Arrays.stream(data.split(DELIMITER)).map(Integer::parseInt).toList();
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String data = callableStatement.getString(i);
        return isEmpty(data) ? new ArrayList<>() : Arrays.stream(data.split(DELIMITER)).map(Integer::parseInt).toList();
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,
                                    CollectionUtils.isEmpty(list) ? null : list.stream().map(String::valueOf).collect(Collectors.joining(DELIMITER)));
    }

    private boolean isEmpty(String data) {
        return data == null || "".equals(data);
    }
}
