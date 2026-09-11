package com.aroolia.performance.batchfetching.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "t_investors")
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
        mappedBy = "investor",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @BatchSize(size = 10)
    private List<Portfolio> portfolios = new ArrayList<>();
    // Constructeur sans argument obligatoire pour la spec JPA
    public Investor() {}

    public Investor(String name) {
        this.name = name;
    }

   

    // Méthode utilitaire synchronisant les deux côtés de la relation bidirectionnelle
    public void addPortfolio(Portfolio portfolio) {
        portfolios.add(portfolio);
        portfolio.setInvestor(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
}