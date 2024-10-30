package br.com.powtec.finance.batch.card_statement.processor;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.powtec.finance.database.library.enums.EntryTypeEnum;
import br.com.powtec.finance.database.library.model.CreditCardInstallmentModel;
import br.com.powtec.finance.database.library.model.movement.CreditCardMovementModel;

@Configuration
public class AssetItemProcessor {

  @Bean
  public ItemProcessor<CreditCardMovementModel, List<CreditCardInstallmentModel>> processor() {
    return movement -> {
      List<CreditCardInstallmentModel> installments = movement.getInstallments();
      if (movement.getInstallments().isEmpty()) {
        // Valor total e número de parcelas
        double valorTotal = movement.getValue();
        int numeroDeParcelas = movement.getInstallment();

        // Divide o valor em parcelas com centavos distribuídos
        List<Double> valoresParcelas = dividirEmParcelas(valorTotal, numeroDeParcelas);
        YearMonth referenceDate;
        if (movement.getDate().getDayOfMonth() == 1) {
          referenceDate = YearMonth.from(movement.getDate());
        } else {
          referenceDate = YearMonth.from(movement.getDate().plusMonths(1));
        }
        // YearMonth firstReferenceDate = 
        // Cria as parcelas com os valores calculados
        for (int i = 0; i < numeroDeParcelas; i++) {
          installments.add(CreditCardInstallmentModel.builder()
              .entryType(EntryTypeEnum.INSTALLMENT)
              .installment(i + 1)
              .movement(movement)
              .referenceDate(referenceDate)
              .value(valoresParcelas.get(i))
              .build());
              referenceDate = referenceDate.plusMonths(1);
        }
      } else {
        return null; // Retorna null caso já existam parcelas
      }

      return installments;
    };
  }

  // Método para dividir o valor em parcelas com centavos distribuídos nas
  // primeiras parcelas
  private List<Double> dividirEmParcelas(double valor, int numeroDeParcelas) {
    List<Double> parcelas = new ArrayList<>();

    // Calcula o valor base de cada parcela
    double valorBase = Math.floor(valor / numeroDeParcelas * 100) / 100.0;

    // Calcula o valor restante que precisará ser distribuído como centavos extras
    double somaParcelasBase = valorBase * numeroDeParcelas;
    double valorRestante = Math.round((valor - somaParcelasBase) * 100);

    // Distribui as parcelas
    for (int i = 0; i < numeroDeParcelas; i++) {
      if (i < valorRestante) {
        parcelas.add(valorBase + 0.01); // Adiciona 1 centavo extra às primeiras parcelas
      } else {
        parcelas.add(valorBase);
      }
    }

    return parcelas;
  }
}