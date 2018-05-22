package org.dreamscale.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.IllegalTransactionStateException
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.DefaultTransactionDefinition
import spock.lang.Specification

import javax.sql.DataSource

@ContextConfiguration(classes = TestConfig.class)
class RequiredTransactorSpec extends Specification {

    @Autowired
    RequiredTransactor transactionalExecutor
    @Autowired
    PlatformTransactionManager transactionManager

    def "perform should wrap block in transaction"() {
        given:
        TransactionDefinition txDef = new DefaultTransactionDefinition()
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY)

        try {
            transactionManager.getTransaction(txDef)
            assert false: "previous call should have failed due to no active transaction"
        } catch (IllegalTransactionStateException ex) {
        }

        when:
        transactionalExecutor.perform({
            transactionManager.getTransaction(txDef)
        })

        then:
        notThrown(IllegalTransactionStateException)
    }

    def "performAndReturn should wrap block in transaction"() {
        given:
        TransactionDefinition txDef = new DefaultTransactionDefinition()
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY)

        try {
            transactionManager.getTransaction(txDef)
            assert false: "previous call should have failed due to no active transaction"
        } catch (IllegalTransactionStateException ex) {
        }

        when:
        transactionalExecutor.performAndReturn({
            transactionManager.getTransaction(txDef)
            return null
        })

        then:
        notThrown(IllegalTransactionStateException)
    }

    @Configuration
    @Import(TransactorConfig.class)
    @EnableTransactionManagement
    static class TestConfig {

        @Bean
        DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .build()
        }

        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource) {
            new DataSourceTransactionManager(dataSource)
        }

    }

}
