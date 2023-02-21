package com.example.springbatchtutorial.job.joblistener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Job 리스너를 사용하여 Job의 실행 전과 실행 후에 로그 남기기
 * run: --spring.batch.job.names=jobListenerJob
 */
@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobListenerJob(final Step jobListenerStep) {
        return jobBuilderFactory.get("jobListenerJob")
                .incrementer(new RunIdIncrementer()) // job의 id를 부여할 때 시퀀스로 부여하도록 하는 설정
                .listener(new JobLoggerListener())
                .start(jobListenerStep)
                .build();
    }

    @Bean
    @JobScope // job의 하위에서 실행됨
    public Step jobListenerStep(final Tasklet jobListenerTasklet) {
        return stepBuilderFactory.get("jobListenerStep")
                .tasklet(jobListenerTasklet) // reader, writer 를 사용하지 않고 단순한 작업만 할 경우 tasklet 사용 가능
                .build();
    }

    @Bean
    @StepScope // step의 하위에서 실행됨
    public Tasklet jobListenerTasklet() {
        return (contribution, chunkContext) -> {
//            System.out.println("Job Listener Tasklet");
//            return RepeatStatus.FINISHED; // 이 스텝을 끝냄

            throw new RuntimeException("Failed!!");
        };
    }
}
