package com.ragukvn.data.manager.batch.config;

import com.ragukvn.data.manager.batch.listener.JobCompletionNotificationListener;
import com.ragukvn.data.manager.entity.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BatchConfigTest {

    private BatchConfig batchConfig;
    private BatchAppConfigs batchAppConfigs;

    @BeforeEach
    void setUp() {
        batchAppConfigs = mock(BatchAppConfigs.class);
        when(batchAppConfigs.getJobName()).thenReturn("testJob");
        when(batchAppConfigs.getChunkSize()).thenReturn(5);
        when(batchAppConfigs.getInputFilePath()).thenReturn("test.csv");
        when(batchAppConfigs.getDelimiter()).thenReturn(",");
        batchConfig = new BatchConfig(batchAppConfigs);
    }

    @Test
    void importJob_shouldCreateJob() {
        JobRepository jobRepository = mock(JobRepository.class);
        Step step = mock(Step.class);
        JobCompletionNotificationListener listener = mock(JobCompletionNotificationListener.class);

        Job job = batchConfig.importJob(jobRepository, step, listener);

        assertThat(job).isNotNull();
    }

    @Test
    void fileLoadStep_shouldCreateStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        FlatFileItemReader<Transaction> reader = mock(FlatFileItemReader.class);
        JpaItemWriter<Transaction> writer = mock(JpaItemWriter.class);

        Step step = batchConfig.fileLoadStep(jobRepository, transactionManager, reader, writer);

        assertThat(step).isNotNull();
    }

    @Test
    void reader_shouldCreateReader() {
        FlatFileItemReader<Transaction> reader = batchConfig.reader();
        assertThat(reader).isNotNull();
    }

    @Test
    void writer_shouldCreateWriter() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        JpaItemWriter<Transaction> writer = batchConfig.writer(emf);
        assertThat(writer).isNotNull();
    }


    @Test
    void headerValidationHandler_shouldFailWhenHeaderIsInvalid() {
        LineCallbackHandler handler = getPrivateHeaderHandler();
        assertThatThrownBy(() -> handler.handleLine("INVALID_HEADER"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid header in input file");
    }

    // Helper to access private headerValidationHandler via reflection
    private LineCallbackHandler getPrivateHeaderHandler() {
        try {
            var method = BatchConfig.class.getDeclaredMethod("headerValidationHandler");
            method.setAccessible(true);
            return (LineCallbackHandler) method.invoke(batchConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
