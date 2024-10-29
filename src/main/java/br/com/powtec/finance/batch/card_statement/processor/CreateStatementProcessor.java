package br.com.powtec.finance.batch.card_statement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.model.CreditCardStatementModel;

@Configuration
public class CreateStatementProcessor {

  @Value("odate")
  private String odate;

@Bean
  public ItemProcessor<CreditCardStatementModel, CreditCardStatementModel> processor() {
    return statement -> {
      
      return null;
    };
  }
}
