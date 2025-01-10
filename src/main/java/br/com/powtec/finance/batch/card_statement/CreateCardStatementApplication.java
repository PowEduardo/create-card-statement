package br.com.powtec.finance.batch.card_statement;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackages = {"br.com.powtec.finance"})
@EnableJpaRepositories(
    basePackages = "br.com.powtec.finance.database.library.repository"
)
@EntityScan(basePackages = {"br.com.powtec.finance.database.library.model"})
public class CreateCardStatementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateCardStatementApplication.class, args);
	}
}
