package ru.igrey.dev.domain;

import ru.igrey.dev.entity.NotificationEntity;

import java.time.LocalDateTime;

public class Notification {
    private String note;
    private LocalDateTime notifyDate;
    private Long userId;
    private Long noteId;

    public Notification(String note, LocalDateTime notifyDate, Long userId, Long noteId) {
        this.note = note;
        this.notifyDate = notifyDate;
        this.userId = userId;
        this.noteId = noteId;
    }

    public NotificationEntity toEntity() {
        return new NotificationEntity(note, notifyDate, userId, noteId);
    }

    public static Notification fromEntity(NotificationEntity entity) {
        return new Notification(entity.getNote(), entity.getNotifyDate(), entity.getUserId(), entity.getNoteId());
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(LocalDateTime notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
}
