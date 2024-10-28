package br.com.powtec.finance.batch.card_statement.writer;

import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class AssetItemWriter {

  @Bean
  public JpaItemWriter<CreditCardMovementModel> writer(EntityManagerFactory entityManagerFactory) {
    JpaItemWriter<CreditCardMovementModel> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(entityManagerFactory);
    return writer;
  }
}
