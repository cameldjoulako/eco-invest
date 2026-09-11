package com.aroolia.performance.batchfetching.repository;

import com.aroolia.performance.batchfetching.model.Investor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {

    // Solution 1 : Requête JPQL explicite avec JOIN FETCH
    @Query("SELECT DISTINCT i FROM Investor i LEFT JOIN FETCH i.portfolios")
    List<Investor> findAllWithPortfoliosByJoinFetch();

    // Solution 2 : Standard JPA déclaratif via @EntityGraph
    @EntityGraph(attributePaths = {"portfolios"})
    @Query("SELECT i FROM Investor i")
    List<Investor> findAllWithPortfoliosByEntityGraph();
}