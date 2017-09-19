package ru.igrey.dev.domain;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.User;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.Language;
import ru.igrey.dev.entity.TelegramUserEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private String language;
    private Integer timezone;

    public TelegramUser(Long userId, String firstName, String lastName, String userName, Boolean isActive, UserStatus status, LocalDateTime createDate, List<Category> categories, String languageCode, String language, Integer timezone) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isActive = isActive;
        this.status = status;
        this.createDate = createDate;
        this.categories = categories;
        this.languageCode = languageCode;
        this.language = language;
        this.timezone = timezone;
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
                user.getLanguageCode(),
                null,
                null);
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
        this.language = userEntity.getLanguage();
        this.timezone = userEntity.getTimezone();
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
                languageCode,
                language,
                timezone
        );
    }

    public String toView() {
        return userId + " fullName: " + firstName + " " + lastName + " userName: " + getUserName();
    }

    public String settings() {
        String currentTime = timezone != null ? LocalDateTime.now(ZoneOffset.UTC).plusMinutes(timezone).format(DateTimeFormatter.ofPattern("HH:mm")) : "-";
        return AnswerMessageText.LANGUAGE.text() + "\n" + AnswerMessageText.LOCAL_TIME.text() + " " + currentTime;
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

    public Language localization() {
        if (StringUtils.isNotBlank(language)) {
            return Language.valueOf(language);
        }
        if (StringUtils.isBlank(languageCode)) {
            return Language.ENGLISH;
        }
        if (languageCode.startsWith("ru")) {
            return Language.RUSSIAN;
        }
        if (languageCode.equals("fa-IR")) {
            return Language.PERSIAN;
        }
        return Language.ENGLISH;

    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println(LocalDateTime.now(utc.getZone()));
        System.out.println(utc.toLocalDateTime());
        System.out.println(LocalDateTime.now(ZoneOffset.UTC));

    }
}
