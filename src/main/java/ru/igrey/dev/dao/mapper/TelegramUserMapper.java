package ru.igrey.dev.dao.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.TelegramUserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by sanasov on 26.04.2017.
 */
public class TelegramUserMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TelegramUserEntity user = new TelegramUserEntity();
        user.setUserId(rs.getLong("ID"));
        user.setFirstName(rs.getString("FIRST_NAME"));
        user.setLastName(rs.getString("LAST_NAME"));
        user.setUserName(rs.getString("USER_NAME"));
        user.setStatus(rs.getString("STATUS"));
        user.setActive(rs.getInt("IS_ACTIVE"));
        user.setLanguageCode(rs.getString("LANGUAGE_CODE"));
        user.setLanguage(rs.getString("LANGUAGE"));
        user.setTimezone(StringUtils.isNotBlank(rs.getString("TIMEZONE")) ? rs.getInt("TIMEZONE") : null);
        user.setCreateDate(LocalDateTime.parse(rs.getString("CREATE_DATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return user;
    }

}