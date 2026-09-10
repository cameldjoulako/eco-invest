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
    @DisplayName("Doit déclencher le problème N+1 lors du parcours des collections Lazy")
    void givenLazyPortfolios_whenGeneratingSummaries_thenTriggersNPlusOne() {
        // GIVEN: 10 investisseurs ont été insérés en base par DataInitializer

        // WHEN: On récupère les résumés (déclenche 1 requête investisseurs + 10 requêtes portefeuilles)
        log.info(">>>>>> DEBUT DU TEST N+1 <<<<<<");
        List<PortfolioSummary> summaries = investorService.getInvestorSummariesWithNPlusOne();
        log.info(">>>>>> FIN DU TEST N+1 <<<<<<");

        // THEN: On vérifie l'intégrité des données retournées
        assertThat(summaries)
                .as("La liste des résumés doit contenir les 10 investisseurs")
                .isNotNull()
                .hasSize(10);

        assertThat(summaries.get(0).portfolioCount())
                .as("Chaque investisseur doit posséder 2 portefeuilles")
                .isEqualTo(2);
    }
}