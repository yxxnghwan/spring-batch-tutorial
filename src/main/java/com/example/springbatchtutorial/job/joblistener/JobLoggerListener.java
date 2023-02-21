package com.example.springbatchtutorial.job.joblistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static final String BEFORE_MESSAGE = "{} Job is Running";
    private static final String AFTER_MESSAGE = "{} Job is Done (Status: {})";

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job is Failed");
        }
    }
}
