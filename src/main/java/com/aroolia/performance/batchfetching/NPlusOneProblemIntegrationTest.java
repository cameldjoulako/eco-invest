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
class NPlusOneProblemIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(NPlusOneProblemIntegrationTest.class);

    @Autowired
    private InvestorService investorService;

    @Test
    @DisplayName("Démonstration du problème N+1 : 1 requête initiale + 10 requêtes unitaires")
    void givenLazyRelation_whenAccessingPortfolios_thenTriggersNPlusOneQueries() {
        log.info(">>>>>> DEBUT DU TEST N+1 <<<<<<");

        List<PortfolioSummary> summaries = investorService.getInvestorSummariesWithNPlusOne();

        log.info(">>>>>> FIN DU TEST N+1 <<<<<<");

        assertThat(summaries).hasSize(10);
        assertThat(summaries.get(0).portfolioCount()).isEqualTo(2);
    }
}