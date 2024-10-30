package br.com.powtec.finance.batch.card_statement.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.powtec.finance.database.library.model.CreditCardInstallmentModel;
import br.com.powtec.finance.database.library.model.CreditCardStatementModel;
import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;

@Configuration
@EnableBatchProcessing
public class BatchJob {

  @Bean
  public Step step1(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<CreditCardMovementModel> reader,
      ItemProcessor<CreditCardMovementModel, List<CreditCardInstallmentModel>> processor,
      ItemWriter<List<CreditCardInstallmentModel>> writer) {
    return new StepBuilder("Create installments for movements not paid and have no installment", jobRepository)
        .<CreditCardMovementModel, List<CreditCardInstallmentModel>>chunk(10, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Step step2(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<String> reader,
      ItemWriter<String> deleteStatement) {
    return new StepBuilder("Delete Statement", jobRepository)
        .<String, String>chunk(1, transactionManager)
        .reader(reader)
        .writer(deleteStatement)
        .build();
  }

  @Bean
  public Step step3(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<CreditCardStatementModel> reader,
      ItemWriter<CreditCardStatementModel> createStatement) {
    return new StepBuilder("Create Statement", jobRepository)
        .<CreditCardStatementModel, CreditCardStatementModel>chunk(1, transactionManager)
        .reader(reader)
        .reader(reader)
        .writer(createStatement)
        .build();
  }

  @Bean
  public Step step4(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<String> reader,
      @Qualifier("updateInstallments") ItemWriter<String> updateInstallments) {
    return new StepBuilder("Join installment with statement", jobRepository)
        .<String, String>chunk(1, transactionManager)
        .reader(reader)
        .writer(updateInstallments)
        .build();
  }

  @Bean
  public Job statementJob(JobRepository jobRepository, Step step1, Step step2, Step step3, Step step4) {
    return new JobBuilder("Create statement card", jobRepository)
        .start(step1)
        .next(step2)
        .next(step3)
        .next(step4)
        .build();
  }
}
