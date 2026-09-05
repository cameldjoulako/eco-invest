package com.aroolia.performance.batchfetching.repository;

import com.aroolia.performance.batchfetching.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
    // utilisation des méthodes standards fournies par JpaRepository (comme findAll)
}