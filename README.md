# EcoInvest : Persistance et Gestion de Portefeuilles

Service métier pour EcoInvest permettant la gestion des investisseurs et la consolidation de leurs portefeuilles d'actifs durables (actions vertes, obligations climat).

Le projet implémente et sécurise la couche d'accès aux données avec Spring Boot, Spring Data JPA et Hibernate, avec une maîtrise stricte des accès SQL pour éliminer le problème du N+1.

## Modèle Métier

Le domaine repose sur une association bidirectionnelle 1-N :
* `Investor` (table `t_investors`) : Identifiant et nom de l'investisseur.
* `Portfolio` (table `t_portfolios`) : Portefeuilles d'actifs rattachés via la clé étrangère `investor_id`.

La relation `@OneToMany` est configurée en `FetchType.LAZY` pour garantir une faible empreinte mémoire lors du chargement initial des investisseurs.

## Cas d'Usage et Optimisations SQL

Le service `InvestorService` expose la génération de résumés d'activité (`PortfolioSummary`) via différentes politiques de chargement :

### 1. Parcours Lazy standard (N+1 Queries)
* Méthode : `getInvestorSummariesWithNPlusOne()`
* Impact : 1 requête pour charger les N investisseurs, suivie de N requêtes individuelles pour lire les portefeuilles de chacun.
* Résultat sur 10 investisseurs : 11 requêtes SQL exécutées.

### 2. Chargement par Lots (Batch Fetching)
* Méthode : `getInvestorSummariesWithNPlusOne()` combinée avec `@BatchSize(size = 10)` sur l'entité ou `hibernate.default_batch_fetch_size: 32` dans `application.yml`.
* Mécanisme : Hibernate intercepte l'accès au premier portefeuille et charge les portefeuilles de tous les investisseurs présents en session via une clause SQL `IN (?, ?, ...)`.
* Résultat sur 10 investisseurs : 2 requêtes SQL au total (1 pour les investisseurs et 1 pour l'ensemble des portefeuilles).

### 3. Chargement Dynamique (JOIN FETCH et EntityGraph)
* Méthode : `getInvestorSummariesWithJoinFetch()` via `InvestorRepository`.
* Mécanisme : Résolution immédiate de l'arbre complet en une seule jointure physique (`LEFT JOIN FETCH` ou `@EntityGraph`).
* Résultat sur 10 investisseurs : 1 unique requête SQL.

## Synthèse Comparative

| Stratégie | Requêtes SQL (pour 10 investisseurs) | Requête SQL générée pour les enfants | Règle d'usage en entreprise |
| :--- | :--- | :--- | :--- |
| **Lazy standard** | 11 | `SELECT ... WHERE investor_id = ?` | À proscrire sur les parcours en boucle |
| **Batch Fetching** | 2 | `SELECT ... WHERE investor_id IN (?, ...)` | Filet de sécurité global de l'architecture |
| **JOIN FETCH** | 1 | Jointure SQL directe (`LEFT OUTER JOIN`) | Recommandé pour les synthèses et bilans complets |

## Suites de Tests d'Intégration

Les tests automatisés JUnit 5 vérifient l'intégrité des résultats et permettent de valider les requêtes émises :

* `NPlusOneProblemIntegrationTest` : Vérifie l'exécution des résumés en mode Lazy et met en évidence la cascade de 11 requêtes SQL.
* `BatchFetchingIntegrationTest` : Valide la réduction drastique à 2 requêtes grâce à la clause `IN` du Batch Fetching.
* `JoinFetchIntegrationTest` : Valide le chargement complet des données en 1 seule requête SQL jointe.

## Démarrage et Exécution

### Prérequis
* Java 25+
* Maven 3.9+

### Exécuter les tests
```bash
mvn clean test