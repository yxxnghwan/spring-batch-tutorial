package com.example.springbatchtutorial.job.validatedparam;

import com.example.springbatchtutorial.job.validatedparam.validator.FileNameParamValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 파일 이름 파라미터 전달 그리고 검증
 * run: --spring.batch.job.names=validatedParamJob -fileName=test.csv
 */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validatedParamJob(final Step validatedParamStep) {
        return jobBuilderFactory.get("validatedParamJob")
                //.validator(new FileNameParamValidator())
                .validator(multipleValidator())
                .incrementer(new RunIdIncrementer()) // job의 id를 부여할 때 시퀀스로 부여하도록 하는 설정
                .start(validatedParamStep)
                .build();
    }

    @Bean
    @JobScope // job의 하위에서 실행됨
    public Step validatedParamStep(final Tasklet validatedParamTasklet) {
        return stepBuilderFactory.get("validatedParamStep")
                .tasklet(validatedParamTasklet) // reader, writer 를 사용하지 않고 단순한 작업만 할 경우 tasklet 사용 가능
                .build();
    }

    @Bean
    @StepScope // step의 하위에서 실행됨
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") final String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println(fileName);
            System.out.println("validated Param Tasklet");
            return RepeatStatus.FINISHED; // 이 스텝을 끝냄
        };
    }

    private CompositeJobParametersValidator multipleValidator() {
        final CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(List.of(new FileNameParamValidator()));

        return validator;
    }
}
