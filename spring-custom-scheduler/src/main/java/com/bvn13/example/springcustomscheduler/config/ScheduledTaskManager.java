package com.bvn13.example.springcustomscheduler.config;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/** Scheduled tasks manager
 * idea: https://dzone.com/articles/schedulers-in-java-and-spring
 * @author boyko_vn
 * @since 05.08.2019
 */
public class ScheduledTaskManager implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskManager.class);

    @Getter
    @Setter
    private int poolSize;

    private TaskScheduler taskScheduler;
    @Getter
    @Setter
    private List<ScheduledTask> scheduling;

    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    /**
     * immutable task list
     * could be used for managing them
     */
    @Getter
    private List<ScheduledFuture<?>> tasks;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);// Set the pool of threads
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();

        taskScheduler = threadPoolTaskScheduler;
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        setUpJobs();
    }

    private void setUpJobs() {
        tasks = Collections.unmodifiableList(scheduling.stream()
                .map(this::prepareJob)
                .collect(Collectors.toList()));
    }

    private ScheduledFuture<?> prepareJob(ScheduledTask task) {
        return taskScheduler.schedule(() -> {
            logger.debug("Starting task: "+task.getName());
            try {
                task.execute();
                logger.debug("Done task: "+task.getName());
            } catch (Throwable e) {
                logger.error("ERROR task: "+task.getName(), e);
            }

        }, triggerContext -> new CronTrigger(task.getCron()).nextExecutionTime(triggerContext));
    }

    public void reloadSchedules() {
        configureTasks(this.scheduledTaskRegistrar);
    }


}
