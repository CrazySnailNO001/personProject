package com.xzh.personalproject.commons.utils;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by xzh on 2017/5/8.
 */
public class DateTimeToUnixTimeTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        LocalDateTime localDateTime;
        localDateTime = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        preparedStatement.setLong(i, localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        Long l = resultSet.getLong(s);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(l), ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        Long l = resultSet.getLong(i);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(l), ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        Long l = callableStatement.getLong(i);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(l), ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
