package br.com.powtec.finance.batch.card_statement.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.powtec.finance.database.library.model.CreditCardInstallmentModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AssetItemWriter implements ItemWriter<List<CreditCardInstallmentModel>> {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void write(@NonNull Chunk<? extends List<CreditCardInstallmentModel>> chunk) throws Exception {
        for (List<CreditCardInstallmentModel> itemList : chunk) {
            itemList.forEach(entityManager::persist);
        }
        entityManager.flush();
    }

}