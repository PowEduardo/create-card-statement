package br.com.powtec.finance.batch.card_statement.reader;

import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.model.CreditCardStatementModel;
import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchReader {

  @Bean
  public JpaPagingItemReader<CreditCardMovementModel> readerMovements(EntityManagerFactory entityManagerFactory) {
    JpaPagingItemReader<CreditCardMovementModel> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setQueryString("SELECT movements FROM CreditCardMovementModel AS movements WHERE paid IS FALSE");
    reader.setPageSize(10); // Define o tamanho da página, leia 10 registros por vez
    reader.setName("CreditCardMovement Reader");
    return reader;
  }

  @Bean
  public JpaPagingItemReader<CreditCardStatementModel> readerStatement(EntityManagerFactory entityManagerFactory,
      @Value("odate") String odate) {
    JpaPagingItemReader<CreditCardStatementModel> reader = new JpaPagingItemReader<>();
    YearMonth referenceDate = YearMonth.parse(odate);
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setQueryString(
        "SELECT statement FROM CreditCardStatementModel AS statement WHERE referenceMonth = :referenceDate");
    reader.setPageSize(10); // Define o tamanho da página, leia 10 registros por vez
    reader.setName("CreditCardStatementModel Reader");
    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("referenceDate", referenceDate);
    reader.setParameterValues(parameterValues);
    return reader;
  }
}
