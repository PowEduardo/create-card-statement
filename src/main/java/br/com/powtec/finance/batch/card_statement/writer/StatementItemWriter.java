package br.com.powtec.finance.batch.card_statement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import br.com.powtec.finance.database.library.model.CreditCardStatementModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class StatementItemWriter {
  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  public ItemWriter<String> deleteStatement() {
    return new ItemWriter<String>() {

      @Transactional
      @Override
      public void write(@NonNull Chunk<? extends String> chunk) throws Exception {
        int rowsAffected = entityManager
            .createQuery("DELETE FROM CreditCardStatementModel WHERE referenceMonth = :referenceMonth")
            .setParameter("referenceMonth", chunk.getItems().get(0))
            .executeUpdate();
        entityManager.flush();

        log.info(rowsAffected + " rows affected");
      }

    };
  }

  @Bean
  public ItemWriter<CreditCardStatementModel> createStatement() {
    return new ItemWriter<CreditCardStatementModel>() {

      @Transactional
      @Override
      public void write(@NonNull Chunk<? extends CreditCardStatementModel> chunk) throws Exception {
        CreditCardStatementModel statementModel = chunk.getItems().get(0);
        entityManager.persist(statementModel);
      }

    };
  }

  @Bean
  public ItemWriter<String> updateInstallments() {
    return new ItemWriter<String>() {

      @Transactional
      @Override
      public void write(@NonNull Chunk<? extends String> chunk) throws Exception {
        int rowsAffected = entityManager.createQuery(
            "UPDATE CreditCardInstallmentModel AS installments SET installments.statement = (SELECT statement FROM CreditCardStatementModel AS statement WHERE statement.referenceMonth = :referenceMonth) WHERE installments.referenceMonth = :referenceMonth")
            .setParameter("referenceMonth", chunk.getItems().get(0))
            .executeUpdate();
        entityManager.flush();
        log.info(rowsAffected + " rows affected");
      }

    };
  }

  @Bean
  public ItemWriter<String> updateStatement() {
    return new ItemWriter<String>() {

      @Transactional
      @Override
      public void write(@NonNull Chunk<? extends String> chunk) throws Exception {
        System.out.println("DEB: " + chunk.getItems().get(0));
        int rowsAffected = entityManager.createQuery(
            "UPDATE CreditCardStatementModel AS statement SET statement.value = (SELECT SUM(installment.value) FROM CreditCardInstallmentModel AS installment WHERE installment.referenceMonth = :referenceMonth) WHERE statement.referenceMonth = :referenceMonth")
            .setParameter("referenceMonth", chunk.getItems().get(0))
            .executeUpdate();
        entityManager.flush();
        log.info(rowsAffected + " rows affected");
      }

    };
  }
}
