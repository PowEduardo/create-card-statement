package br.com.powtec.finance.batch.card_statement.reader;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchReader {

  @Bean
  public JpaPagingItemReader<CreditCardMovementModel> readerMovements(EntityManagerFactory entityManagerFactory) {
    JpaPagingItemReader<CreditCardMovementModel> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setQueryString(
        "SELECT movements FROM CreditCardMovementModel AS movements LEFT JOIN movements.installments AS installments WHERE paid IS FALSE AND installments IS NULL");
    reader.setPageSize(10); // Define o tamanho da página, leia 10 registros por vez
    reader.setName("CreditCardMovement Reader");
    return reader;
  }

}
