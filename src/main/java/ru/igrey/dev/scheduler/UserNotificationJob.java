package ru.igrey.dev.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.igrey.dev.keyboard.ReplyKeyboard;
import ru.igrey.dev.config.BeanConfig;
import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.dao.repository.NotificationRepository;
import ru.igrey.dev.domain.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserNotificationJob implements Job {

    NotificationRepository notificationRepository;

    public UserNotificationJob() {
        this.notificationRepository = BeanConfig.notificationRepository();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Notification> expiredNotifications = notificationRepository.findAll().stream()
                .filter(notification -> notification.getNotifyDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        for (Notification notification : expiredNotifications) {
            sendNotification(notification);
            notificationRepository.delete(notification.getNoteId());
        }

    }

    private void sendNotification(Notification notification) {
        log.info("userId: " + notification.getUserId());
        log.info("send notification: " + notification.getNote());
        BeanConfig.tellMeBot()
                .sendButtonMessage(
                        notification.getUserId(),
                        Emoji.BELL.toString(3) + " " + notification.getNote() + "\n\n" + AnswerMessageText.NOTIFY_AGAIN_IN.text(),
                        ReplyKeyboard.buttonsRemindAgainIn(notification.getNoteId()));
    }

}