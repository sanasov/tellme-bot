package ru.igrey.dev.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;

public class JobFactory {

    public static JobDetail createNotificationJob() {
        return JobBuilder.newJob(UserNotificationJob.class)
                .withIdentity("notification", GroupName.NOTIFICATION_GROUP).build();
    }
}
