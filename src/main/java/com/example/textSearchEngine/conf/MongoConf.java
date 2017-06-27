package com.example.textSearchEngine.conf;

import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suryansh on 27/6/17.
 */
/*@Configuration
public class MongoConf extends AbstractMongoConfiguration {

    private final Logger log = LoggerFactory.getLogger(MongoConf.class);

    @Autowired
    MongoMappingContext mappingContext;

    @Autowired
    private MongoDbFactory mongoDbFactory;


    @Override
    protected String getDatabaseName() {
        return mongoDbFactory.getDb().getName();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongoDbFactory.getDb().getMongo();
    }

    @Bean
    public MappingMongoConverter mongoConverter(MongoDbFactory mongoFactory) throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mongoConverter.setMapKeyDotReplacement("$");
        return mongoConverter;
    }
}*/


@Configuration
public class MongoConf {

    @Autowired
    private MongoDbFactory mongoFactory;

    @Autowired
    private MongoMappingContext mongoMappingContext;

    @Bean
    public MappingMongoConverter mongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //this is my customization
        mongoConverter.setMapKeyDotReplacement("__");
        mongoConverter.afterPropertiesSet();
        return mongoConverter;
    }
}