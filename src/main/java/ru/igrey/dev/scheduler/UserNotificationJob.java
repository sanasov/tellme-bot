package ru.igrey.dev.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.igrey.dev.config.BeanConfig;

public class UserNotificationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        BeanConfig.tellMeBot().sendNotification();
    }

}