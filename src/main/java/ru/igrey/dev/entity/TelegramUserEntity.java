package ru.igrey.dev.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * Created by sanasov on 26.04.2017.
 */
@EqualsAndHashCode
@ToString
public class TelegramUserEntity {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Integer isActive;
    private String status;
    private LocalDateTime createDate;
    private String languageCode;

    public TelegramUserEntity(Long userId, String firstName, String lastName, String userName, String status, LocalDateTime createDate, Integer isActive, String languageCode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.status = status;
        this.createDate = createDate;
        this.isActive = isActive;
        this.languageCode = languageCode;
    }

    public TelegramUserEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getActive() {
        return isActive;
    }

    public void setActive(Integer active) {
        isActive = active;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return StringUtils.isNotBlank(languageCode) ? languageCode : "";
    }
}
