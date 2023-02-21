package com.example.springbatchtutorial.job.helloworld;

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
 * Hello World를 출력
 * run: --spring.batch.job.names=helloWorldJob
 */
@Configuration
@RequiredArgsConstructor
public class HelloWordJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("helloWorldJob")
                .incrementer(new RunIdIncrementer()) // job의 id를 부여할 때 시퀀스로 부여하도록 하는 설정
                .start(helloWorldStep())
                .build();
    }

    @Bean
    @JobScope // job의 하위에서 실행됨
    public Step helloWorldStep() {
        return stepBuilderFactory.get("helloWorldStep")
                .tasklet(helloWorldTasklet()) // reader, writer 를 사용하지 않고 단순한 작업만 할 경우 tasklet 사용 가능
                .build();
    }

    @Bean
    @StepScope // step의 하위에서 실행됨
    public Tasklet helloWorldTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello World Spring Batch");
            return RepeatStatus.FINISHED; // 이 스텝을 끝냄
        };
    }
}
