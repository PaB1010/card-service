package org.anonymous.global.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * DB
 *
 * JPA Query Factory - Query dsl (Query building)
 *
 * 하나 이상의 복잡한 Query 사용시 사용
 * QuerydslPredicateExecutor 로는 복잡한 Query 사용은 한계가 있음
 *
 */
@Configuration
public class DBConfig {

    @PersistenceContext
    private EntityManager em;

    @Lazy
    @Bean
    public JPAQueryFactory jpaQueryFactory() {

        return new JPAQueryFactory(em);
    }
}