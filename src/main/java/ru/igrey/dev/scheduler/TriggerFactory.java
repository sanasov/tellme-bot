package ru.igrey.dev.scheduler;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class TriggerFactory {

    public static Trigger createTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("repeatTrigger", GroupName.NOTIFICATION_GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(30).repeatForever())
                .build();
    }

}
