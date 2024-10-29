package br.com.powtec.finance.batch.card_statement.reader;

import java.time.YearMonth;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import br.com.powtec.finance.database.library.model.CreditCardModel;
import br.com.powtec.finance.database.library.model.CreditCardStatementModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CreateStatementIfNotExistsReader implements ItemReader<CreditCardStatementModel> {

  private String odate;
  private final JpaPagingItemReader<CreditCardStatementModel> delegate;
  private EntityManager entityManager;

  public CreateStatementIfNotExistsReader(JpaPagingItemReader<CreditCardStatementModel> delegate, EntityManager entityManager, String odate) {
    this.delegate = delegate;
    this.entityManager = entityManager;
    this.odate = odate;
  }

  @Override
  @Nullable
  public CreditCardStatementModel read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    TypedQuery<Long> query = entityManager.createQuery(
        "SELECT COUNT(e) FROM CreditCardStatementModel e WHERE e.referenceMonth = :referenceMonth", Long.class);
    query.setParameter("referenceMonth", YearMonth.parse(odate));
    Long count = query.getSingleResult();
    if (count == 0) {
      entityManager.persist(CreditCardStatementModel.builder()
          .card(CreditCardModel.builder().id(1L).build())
          .paid(false)
          .referenceMonth(YearMonth.parse(odate))
          .build());
    }
    return delegate.read();
  }

}
