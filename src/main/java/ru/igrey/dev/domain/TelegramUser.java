package ru.igrey.dev.domain;

import ru.igrey.dev.entity.TelegramUserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class TelegramUser {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Boolean isActive;
    private String status;
    private LocalDateTime createDate;
    private List<Category> categories;

    public TelegramUser(Long userId, String firstName, String lastName, String userName, Boolean isActive, String status, LocalDateTime createDate, List<Category> categories) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isActive = isActive;
        this.status = status;
        this.createDate = createDate;
        this.categories = categories;
    }


    public TelegramUser(TelegramUserEntity userEntity, List<Category> categories) {
        this.userId = userEntity.getUserId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.userName = userEntity.getUserName();
        this.isActive = userEntity.getActive();
        this.status = userEntity.getStatus();
        this.createDate = userEntity.getCreateDate();
        this.categories = categories;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
