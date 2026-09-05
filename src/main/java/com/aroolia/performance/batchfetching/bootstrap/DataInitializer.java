package com.aroolia.performance.batchfetching.bootstrap;

import com.aroolia.performance.batchfetching.model.Investor;
import com.aroolia.performance.batchfetching.model.Portfolio;
import com.aroolia.performance.batchfetching.repository.InvestorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final InvestorRepository investorRepository;

    public DataInitializer(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("--- Initialisation du jeu de données EcoInvest ---");

        for (int i = 1; i <= 10; i++) {
            Investor investor = new Investor("Investisseur_" + i);

            Portfolio actionsVertes = new Portfolio("Portefeuille_Actions_Vertes_" + i, investor);
            Portfolio obligationsClimat = new Portfolio("Portefeuille_Obligations_Climat_" + i, investor);

            investor.addPortfolio(actionsVertes);
            investor.addPortfolio(obligationsClimat);

            investorRepository.save(investor);
        }

        log.info("--- 10 investisseurs et 20 portefeuilles créés avec succès ---");
    }
}