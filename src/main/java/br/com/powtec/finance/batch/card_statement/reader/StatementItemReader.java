package br.com.powtec.finance.batch.card_statement.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import br.com.powtec.finance.database.library.model.CreditCardStatementModel;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class StatementItemReader {

  @Bean
  public ItemReader<String> readerOdate(EntityManagerFactory entityManagerFactory,
      @Value("${odate}") String odate) {
    return new ItemReader<String>() {
      private boolean read = false;

      @Override
      @Nullable
      public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!read) {
          read = true; // Mark as read after the first call
          return odate;
        }
        read = false;
        return null; // Return null on subsequent calls

      }

    };

  }

  @Bean
  public ItemReader<CreditCardStatementModel> createStatementModel(EntityManagerFactory entityManagerFactory,
      @Value("${odate}") String odate) {
    return new ItemReader<CreditCardStatementModel>() {
      private boolean read = false;

      @Override
      @Nullable
      public CreditCardStatementModel read()
          throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!read) {
          read = true;
          return CreditCardStatementModel.builder()
              .referenceMonth(odate)
              .paid(false)
              .build();
        }
        read = false;
        return null;
      }

    };
  }
}
