package ru.igrey.dev.domain;

import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.HtmlWrapper;
import ru.igrey.dev.constant.Delimiter;
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.entity.NoteEntity;
import ru.igrey.dev.domain.notifyrule.NotifyRule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Note {
    private Long id;
    private Long categoryId;
    private Long userId;
    private LocalDateTime createDate;
    private String text;
    private String notifyRule;
    private String fileName;
    private String caption;

    public Note(Long id, Long categoryId, Long userId, LocalDateTime createDate, String text, String notifyRule, String fileName, String capture) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.createDate = createDate;
        this.text = text;
        this.notifyRule = notifyRule;
        this.fileName = fileName;
        this.caption = capture;
    }

    public Note(NoteEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.categoryId = entity.getCategoryId();
        this.createDate = entity.getCreateDate();
        this.text = entity.getText();
        this.notifyRule = entity.getNotifyRule();
        this.fileName = entity.getFileName();
        this.caption = entity.getCaption();
    }

    public static Note createNewNote(String text, String fileName, String capture, Long categoryId, Long userId) {
        String[] notifyRuleAndNoteName = text.split(Delimiter.NOTIFY_DELIMITER);
        if (notifyRuleAndNoteName.length == 2) {
            return new Note(null, categoryId, userId, null, notifyRuleAndNoteName[1].trim(), notifyRuleAndNoteName[0].trim(), fileName, capture);
        }
        return new Note(null, categoryId, userId, null, text, null, fileName, capture);
    }

    public List<Notification> createNotifications() {
        NotifyRule rule = NotifyRule.buildNotifyRule(this.notifyRule);
        if (rule == null || rule.getPeriodical()) {
            return new ArrayList<>();
        }
        return Optional.ofNullable(rule.getNotifyDates()).orElse(new ArrayList<>())
                .stream()
                .map(notificationDate -> new Notification(this.text,
                        LocalDateTime.of(notificationDate, rule.getTime()),
                        this.userId,
                        this.id))
                .collect(Collectors.toList());
    }


    public NoteEntity toEntity() {
        return new NoteEntity(id, createDate, text, categoryId, userId, notifyRule, fileName, caption);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNotifyRule() {
        return notifyRule;
    }

    public void setNotifyRule(String notifyRule) {
        this.notifyRule = notifyRule;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String toView() {
        List<Notification> notifications = createNotifications();
        String notificationDates = notificationDatesView(notifications);
        String bell = "";
        if (StringUtils.isNotBlank(notificationDates)) {
            bell = Emoji.BELL.toString();
            notificationDates = HtmlWrapper.toInlineFixedWidthCode("(" + notificationDates + ")");
        }
        return bell
                + HtmlWrapper.htmlSafe(text)
                + " "
                + notificationDates;
    }

    public String toViewForDelete() {
        List<Notification> notifications = createNotifications();
        String notificationDates = notificationDatesView(notifications);
        String bell = "";
        if (StringUtils.isNotBlank(notificationDates)) {
            bell = Emoji.BELL.toString();
            notificationDates = "(" + notificationDates + ")";
        }
        return bell
                + HtmlWrapper.htmlSafe(text)
                + " "
                + notificationDates;
    }

    private String notificationDatesView(List<Notification> notifications) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
        return Optional.ofNullable(notifications).orElse(new ArrayList<>())
                .stream()
                .filter(notification -> notification.getNotifyDate().isAfter(LocalDateTime.now()))
                .map(notification -> notification.getNotifyDate().format(formatter))
                .reduce((total, curr) -> total + ", " + curr)
                .orElse(notifications != null && notifications.size() > 0 ? "expired" : "");
    }

    public String toFileView() {
        if (StringUtils.isNotBlank(caption)) {
            return caption;
        } else {
            return fileName;
        }
    }


}
