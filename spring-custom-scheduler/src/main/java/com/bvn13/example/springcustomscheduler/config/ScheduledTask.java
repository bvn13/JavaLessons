package com.bvn13.example.springcustomscheduler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanNameAware;

/** Scheduled task
 *
 * @author boyko_vn
 * @since 05.08.2019
 */
abstract public class ScheduledTask implements BeanNameAware {

    /** task name */
    @Getter
    @Setter
    private String name;

    /** schedule */
    @Getter
    @Setter
    private String cron;

    @Override
    public void setBeanName(String s) {
        name = s;
    }

    abstract void execute() throws Exception;

}
