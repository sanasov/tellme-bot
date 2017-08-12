package ru.igrey.dev.entity;

import java.time.LocalDateTime;

public class CategoryEntity {

    private Long id;
    private Long userId;
    private LocalDateTime createDate;
    private String title;

    public CategoryEntity(Long id, Long userId, LocalDateTime createDate, String title) {
        this.id = id;
        this.userId = userId;
        this.createDate = createDate;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
