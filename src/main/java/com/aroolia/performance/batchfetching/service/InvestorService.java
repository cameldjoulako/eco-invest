package com.aroolia.performance.batchfetching.service;

import com.aroolia.performance.batchfetching.dto.PortfolioSummary;
import com.aroolia.performance.batchfetching.model.Investor;
import com.aroolia.performance.batchfetching.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    /**
     * Usecase métier : Générer le résumé des portefeuilles pour chaque investisseur.
     * Déclenche le problème N+1 en mode Lazy classique sans optimisation.
     */
    @Transactional(readOnly = true)
    public List<PortfolioSummary> getInvestorSummariesWithNPlusOne() {
        // Requête 1 : Récupère les 10 investisseurs
        List<Investor> investors = investorRepository.findAll();

        // Pour chacun des N investisseurs, l'appel à .getPortfolios().size() force l'initialisation du Proxy
        // Déclenche N requêtes SQL supplémentaires !
        return investors.stream()
                .map(investor -> new PortfolioSummary(
                        investor.getName(),
                        investor.getPortfolios().size()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Cas d'utilisation optimisé via JOIN FETCH :
     * Tout l'arbre d'objets est chargé en une unique requête SQL.
     */
    @Transactional(readOnly = true)
    public List<PortfolioSummary> getInvestorSummariesWithJoinFetch() {
        List<Investor> investors = investorRepository.findAllWithPortfoliosByJoinFetch();

        return investors.stream()
                .map(investor -> new PortfolioSummary(
                        investor.getName(),
                        investor.getPortfolios().size()
                ))
                .collect(Collectors.toList());
    }
}