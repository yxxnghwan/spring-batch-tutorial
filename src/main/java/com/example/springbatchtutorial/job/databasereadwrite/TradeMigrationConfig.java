package com.example.springbatchtutorial.job.databasereadwrite;

import com.example.springbatchtutorial.domain.account.Account;
import com.example.springbatchtutorial.domain.account.AccountRepository;
import com.example.springbatchtutorial.domain.order.Order;
import com.example.springbatchtutorial.domain.order.OrderRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

/**
 * 주문 Table -> 정산 Table 로 데이터 이관
 * run: --spring.batch.job.names=tradeMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class TradeMigrationConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    @Bean
    public Job tradeMigrationJob() {
        return jobBuilderFactory.get("tradeMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(tradeMigrationStep())
                .build();
    }

    @Bean
    @JobScope
    public Step tradeMigrationStep() {
        return stepBuilderFactory.get("tradeMigrationStep")
                .<Order, Account>chunk(5) // <읽을 데이터 타입, 쓸 데이터 타입> chunkSize: 각 커밋 사이에 처리되는 row 수
                .reader(tradeOrderReader())
                .processor(tradeOrderProcessor())
                .writer(tradeOrderWriter())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Order> tradeOrderReader() {
        return new RepositoryItemReaderBuilder<Order>()
                .name("tradeOrderReader")
                .repository(orderRepository)
                .methodName("findAll")
                .arguments(List.of()) // Repository에 정의된 메서드에 들어가는 파라미터
                .pageSize(5)
                .sorts(Collections.singletonMap("id", Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Order, Account> tradeOrderProcessor() {
        return Account::new;
    }

//    @Bean
//    @StepScope
//    public RepositoryItemWriter<Account> tradeOrderWriter() {
//        return new RepositoryItemWriterBuilder<Account>()
//                .repository(accountRepository)
//                .methodName("save")
//                .build();
//    }

    @Bean
    @StepScope
    public ItemWriter<Account> tradeOrderWriter() {
        return items -> {
            items.forEach(accountRepository::save);
        };
    }
}
