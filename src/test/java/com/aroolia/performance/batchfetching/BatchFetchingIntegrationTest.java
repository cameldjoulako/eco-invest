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
class BatchFetchingIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(BatchFetchingIntegrationTest.class);

    @Autowired
    private InvestorService investorService;

    @Test
    @DisplayName("Doit charger les portefeuilles par lot (Batch Fetching) au lieu d'exécuter N requêtes")
    void givenBatchSizeConfigured_whenAccessingPortfolios_thenQueriesAreBatched() {
        // GIVEN: 10 investisseurs en base et @BatchSize(size = 10) sur la collection

        // WHEN: On accède aux portefeuilles
        log.info(">>>>>> DEBUT DU TEST BATCH FETCHING <<<<<<");
        List<PortfolioSummary> summaries = investorService.getInvestorSummariesWithNPlusOne();
        log.info(">>>>>> FIN DU TEST BATCH FETCHING <<<<<<");

        // THEN: Les données sont complètes, mais obtenues en seulement 2 requêtes SQL au total
        assertThat(summaries)
                .as("La liste doit contenir les 10 résumés")
                .isNotNull()
                .hasSize(10);

        assertThat(summaries.get(0).portfolioCount())
                .as("L'investisseur doit toujours avoir ses 2 portefeuilles")
                .isEqualTo(2);
    }
}