package ru.igrey.dev.domain;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.User;
import ru.igrey.dev.entity.TelegramUserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"categories", "isActive", "status"})
public class TelegramUser {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Boolean isActive;
    private UserStatus status;
    private LocalDateTime createDate;
    private List<Category> categories;
    private String languageCode;

    public TelegramUser(Long userId, String firstName, String lastName, String userName, Boolean isActive, UserStatus status, LocalDateTime createDate, List<Category> categories, String languageCode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isActive = isActive;
        this.status = status;
        this.createDate = createDate;
        this.categories = categories;
        this.languageCode = languageCode;
    }

    public static TelegramUser createNewUser(User user) {
        return new TelegramUser(user.getId().longValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                null,
                UserStatus.NEW,
                null,
                null,
                user.getLanguageCode());
    }


    public TelegramUser(TelegramUserEntity userEntity, List<Category> categories) {
        this.userId = userEntity.getUserId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.userName = userEntity.getUserName();
        this.status = UserStatus.valueOf(userEntity.getStatus());
        this.createDate = userEntity.getCreateDate();
        this.categories = categories;
        this.languageCode = userEntity.getLanguageCode();
    }

    public TelegramUserEntity toEntity() {
        return new TelegramUserEntity(
                userId,
                firstName,
                lastName,
                userName,
                status.name(),
                createDate,
                null,
                languageCode
        );
    }

    public String toView() {
        return "userId: " + userId + " fullName: " + firstName + " " + lastName + " userName: " + getUserName();
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
        return StringUtils.isNotBlank(userName) ? userName : "";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<Category> getCategories() {
        return categories == null ? new ArrayList<>() : categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
