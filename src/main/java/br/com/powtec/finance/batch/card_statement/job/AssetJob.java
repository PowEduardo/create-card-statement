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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.powtec.finance.database.library.model.CreditCardInstallmentModel;
import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;

@Configuration
@EnableBatchProcessing
public class AssetJob {

  @Bean
  public Step step1(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<CreditCardMovementModel> reader,
      ItemProcessor<CreditCardMovementModel, List<CreditCardInstallmentModel>> processor,
      ItemWriter<List<CreditCardInstallmentModel>> writer) {
    return new StepBuilder("Get movement not paid", jobRepository)
        .<CreditCardMovementModel, List<CreditCardInstallmentModel>>chunk(10, transactionManager) // Processa 10 registros por vez
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Job assetPrice(JobRepository jobRepository, Step step1) {
    return new JobBuilder("Create statement card", jobRepository)
        .start(step1)
        .build();
  }
}
