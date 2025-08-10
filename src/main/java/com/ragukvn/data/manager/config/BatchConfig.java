package com.ragukvn.data.manager.config;

import com.ragukvn.data.manager.entity.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class BatchConfig {
    // @EnableBatchProcessing is not used here as it is deprecated in Spring Boot 3.x

    @Value("${file.input}")
    private String inputFilePath;

    @Bean
    public Job importJob(JobRepository jobRepository, Step fileLoadStep) {
        return new JobBuilder("importTransactionsJob", jobRepository)
                .start(fileLoadStep)
                .build();
    }

    @Bean
    public Step fileLoadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                             FlatFileItemReader<Transaction> reader,
                             JpaItemWriter<Transaction> writer) {

        return new StepBuilder("file-load-step", jobRepository)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public JpaItemWriter<Transaction> writer(EntityManagerFactory entityManager) {
        return new JpaItemWriterBuilder<Transaction>()
                .entityManagerFactory(entityManager)
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> reader() {
        // Configure the FlatFileItemReader here
        BeanWrapperFieldSetMapper<Transaction> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Transaction.class);

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(new ClassPathResource(inputFilePath))
                .linesToSkip(1)
                .delimited()
                .delimiter("|")
                .names("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId")
                .fieldSetMapper(fieldSet -> Transaction.builder()
                        .accountNumber(fieldSet.readString("accountNumber"))
                        .trxAmount(fieldSet.readBigDecimal("trxAmount"))
                        .description(fieldSet.readString("description"))
                        .trxDate(LocalDate.parse(fieldSet.readString("trxDate")))
                        .trxTime(LocalTime.parse(fieldSet.readString("trxTime")))
                        .customerId(fieldSet.readLong("customerId"))
                        .build()
                )
                .build();
    }
}
