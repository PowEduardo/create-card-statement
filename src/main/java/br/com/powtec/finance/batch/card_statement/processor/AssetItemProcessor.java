package br.com.powtec.finance.batch.card_statement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;

@Configuration
public class AssetItemProcessor {

  @Bean
  public ItemProcessor<CreditCardMovementModel, CreditCardMovementModel> processor() {
    return movement -> {
      System.out.println("");
      return movement;
    };
  }
}
