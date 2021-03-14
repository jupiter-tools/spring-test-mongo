package com.jupiter.tools.spring.test.mongo.junit5;

import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner.ReflectionsDocumentScanner;
import com.jupiter.tools.spring.test.mongo.junit5.MongoDbTcExtensionIT.MongoTransactionTestConfig;
import com.jupiter.tools.spring.test.mongo.junit5.MongoDbTcExtensionIT.MongoTransactionTestConfig.TransactionalExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtension.class)
@Import(MongoTransactionTestConfig.class)
@EnableMongoDbTestContainers
class MongoDbTcExtensionIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoTransactionManager transactionManager;

    @Autowired
    private TransactionalExecutor transactionalExecutor;

    @Test
    @MongoDataSet(cleanBefore = true, cleanAfter = true)
    void saveWithExceptionTest() {
        // Arrange
        assertFooCollectionEmpty();

        // Act
        assertThrows(
                RuntimeException.class,
                () -> {
                    mongoTemplate.save(new Foo());
                    throw new RuntimeException("Some error");
                });

        // Assert
        assertFooCollectionNotEmpty();
    }

    @Test
    @MongoDataSet(cleanBefore = true, cleanAfter = true)
    void saveWithExceptionInManualTransactionTest() {
        // Arrange
        assertFooCollectionEmpty();

        // Act
        assertThrows(
                RuntimeException.class,
                () -> doInTransaction(
                        () -> {
                            mongoTemplate.save(new Foo());

                            throw new RuntimeException("Some error");
                        }));

        // Assert
        assertFooCollectionEmpty();
    }

    @Test
    @MongoDataSet(cleanBefore = true, cleanAfter = true)
    void saveWithExceptionInAnnotateTransactionTest() {
        // Arrange
        assertFooCollectionEmpty();

        // Act
        assertThrows(
                RuntimeException.class,
                () -> transactionalExecutor.doInTransaction(
                        () -> {
                            mongoTemplate.save(new Foo());

                            throw new RuntimeException("Some error");
                        }));

        // Assert
        assertFooCollectionEmpty();
    }

    private void assertFooCollectionEmpty() {
        List<Foo> after = mongoTemplate.findAll(Foo.class);
        assertThat(after).isEmpty();
    }

    private void assertFooCollectionNotEmpty() {
        List<Foo> after = mongoTemplate.findAll(Foo.class);
        assertThat(after).isNotEmpty();
    }

    private void doInTransaction(Runnable runnable) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                runnable.run();
            }
        });
    }

    public static class MongoTransactionTestConfig {

        public static class TransactionalExecutor {

            @Transactional
            public void doInTransaction(Runnable runnable) {
                runnable.run();
            }
        }

        @Bean
        public TransactionalExecutor transactionalExecutor() {
            return new TransactionalExecutor();
        }

        @Bean
        public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
            return new MongoTransactionManager(dbFactory);
        }

        @Bean
        public SmartInitializingSingleton createMongoCollectionsOnStartup(MongoTemplate mongoTemplate) {

            return () -> {
                Map<String, Class<?>> classes = new ReflectionsDocumentScanner("").scan();

                classes.values()
                       .forEach(clazz -> {
                           if (!mongoTemplate.collectionExists(clazz)) {
                               mongoTemplate.createCollection(clazz);
                           }
                       });
            };
        }
    }
}