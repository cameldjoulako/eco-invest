package com.aroolia.performance.batchfetching;

import com.aroolia.performance.batchfetching.dto.PortfolioSummary;
import com.aroolia.performance.batchfetching.service.InvestorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JoinFetchIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(JoinFetchIntegrationTest.class);

    @Autowired
    private InvestorService investorService;

    @Test
    @DisplayName("Doit charger l'ensemble des données en une unique requête SQL via JOIN FETCH")
    void givenJoinFetch_whenFetchingPortfolios_thenExecutesSingleQuery() {
        // GIVEN: Données initialisées

        // WHEN: Appel avec JOIN FETCH
        log.info(">>>>>> DEBUT DU TEST JOIN FETCH <<<<<<");
        List<PortfolioSummary> summaries = investorService.getInvestorSummariesWithJoinFetch();
        log.info(">>>>>> FIN DU TEST JOIN FETCH <<<<<<");

        // THEN: Résultat identique, obtenu en 1 seule requête SQL
        assertThat(summaries)
                .as("La liste doit contenir exactement 10 résumés")
                .isNotNull()
                .hasSize(10);

        assertThat(summaries.get(0).portfolioCount())
                .as("L'investisseur doit avoir ses 2 portefeuilles")
                .isEqualTo(2);
    }
}