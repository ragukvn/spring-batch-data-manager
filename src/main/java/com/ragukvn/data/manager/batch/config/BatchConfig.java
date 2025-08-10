package com.ragukvn.data.manager.batch.config;

import com.ragukvn.data.manager.batch.listener.JobCompletionNotificationListener;
import com.ragukvn.data.manager.entity.Transaction;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.ACCOUNT_NUMBER;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.CUSTOMER_ID;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.DESCRIPTION;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.LINES_TO_SKIP;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.LOAD_STEP_NAME;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.READER_NAME;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.TRX_AMOUNT;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.TRX_DATE;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.TRX_TIME;
import static com.ragukvn.data.manager.batch.constant.BatchAppConstants.VALID_FIELD_ORDER;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    // @EnableBatchProcessing is not used here as it is deprecated in Spring Boot 3.x

    private final BatchAppConfigs batchAppConfigs;

    private static final String[] FIELD_NAMES = {ACCOUNT_NUMBER, TRX_AMOUNT, DESCRIPTION, TRX_DATE, TRX_TIME, CUSTOMER_ID};

    @Bean
    public Job importJob(JobRepository jobRepository, Step fileLoadStep, JobCompletionNotificationListener listener) {
        return new JobBuilder(batchAppConfigs.getJobName(), jobRepository)
                .listener(listener)
                .start(fileLoadStep)
                .build();
    }

    @Bean
    public Step fileLoadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                             FlatFileItemReader<Transaction> reader,
                             JpaItemWriter<Transaction> writer) {

        return new StepBuilder(LOAD_STEP_NAME, jobRepository)
                .<Transaction, Transaction>chunk(batchAppConfigs.getChunkSize(), transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> reader() {
        // Configure the FlatFileItemReader here
        BeanWrapperFieldSetMapper<Transaction> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Transaction.class);

        return new FlatFileItemReaderBuilder<Transaction>()
                .name(READER_NAME)
                .resource(new ClassPathResource(batchAppConfigs.getInputFilePath()))
                .linesToSkip(LINES_TO_SKIP)
                .skippedLinesCallback(headerValidationHandler())
                .delimited()
                .delimiter(batchAppConfigs.getDelimiter())
                .names(FIELD_NAMES)
                .fieldSetMapper(fieldSet -> Transaction.builder()
                        .accountNumber(fieldSet.readString(ACCOUNT_NUMBER))
                        .trxAmount(fieldSet.readBigDecimal(TRX_AMOUNT))
                        .description(fieldSet.readString(DESCRIPTION))
                        .trxDate(LocalDate.parse(fieldSet.readString(TRX_DATE)))
                        .trxTime(LocalTime.parse(fieldSet.readString(TRX_TIME)))
                        .customerId(fieldSet.readLong(CUSTOMER_ID))
                        .build()
                )
                .strict(true)
                .build();
    }

    @Bean
    public JpaItemWriter<Transaction> writer(EntityManagerFactory entityManager) {
        return new JpaItemWriterBuilder<Transaction>()
                .entityManagerFactory(entityManager)
                .build();
    }


    private LineCallbackHandler headerValidationHandler() {
        return line -> {
            if (!line.equals(VALID_FIELD_ORDER)) {
                throw new IllegalArgumentException("Invalid header in input file. Expected: " + VALID_FIELD_ORDER + ", but found: " + line);
            }
        };
    }

}
