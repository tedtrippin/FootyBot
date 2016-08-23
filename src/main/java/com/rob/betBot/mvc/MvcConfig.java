package com.rob.betBot.mvc;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.rob.betBot.EventLoader;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.dao.EventDao;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.dao.RaceTimeDao;
import com.rob.betBot.dao.RunnerDao;
import com.rob.betBot.dao.WhEventParentDao;
import com.rob.betBot.dao.hibernate.HibernateEventDao;
import com.rob.betBot.dao.hibernate.HibernateIdGenerator;
import com.rob.betBot.dao.hibernate.HibernateMarketDao;
import com.rob.betBot.dao.hibernate.HibernateRaceTimeDao;
import com.rob.betBot.dao.hibernate.HibernateRunnerDao;
import com.rob.betBot.dao.hibernate.HibernateWhEventParentDao;
import com.rob.betBot.exchange.betfair.BetfairFootballConverter;
import com.rob.betBot.exchange.betfair.BetfairHelper;
import com.rob.betBot.exchange.betfair.BetfairHorseRacingConverter;
import com.rob.betBot.exchange.williamHill.WilliamHillEventLoader;
import com.rob.betBot.exchange.williamHill.WilliamHillFootyEventManager;
import com.rob.betBot.exchange.williamHill.WilliamHillPricesManager;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.Prices;
import com.rob.betBot.model.PricesData;
import com.rob.betBot.model.RaceTimeData;
import com.rob.betBot.model.RunnerData;
import com.rob.betBot.model.wh.WhEventParentData;

/**
 * This is the config used for springs main dispatcher servlet.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.rob.betBot.mvc", "com.rob.betBot.rest" })
@EnableTransactionManagement
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public EventDao eventDao() {
        return new HibernateEventDao();
    }

    @Bean
    public RaceTimeDao raceTimeDao() {
        return new HibernateRaceTimeDao();
    }

    @Bean
    public MarketDao marketDao() {
        return new HibernateMarketDao();
    }

    @Bean
    public RunnerDao runnerDao() {
        return new HibernateRunnerDao();
    }

    @Bean
    public IdGenerator idGenerator() {
        return new HibernateIdGenerator();
    }

    @Bean
    public EventLoader eventLoader() {
        return new EventLoader();
    }

    @Bean
    public WilliamHillFootyEventManager williamHillFootyEventManager() {
        return new WilliamHillFootyEventManager();
    }

    @Bean
    public WilliamHillEventLoader williamHillEventLoader() {
        return new WilliamHillEventLoader();
    }

    @Bean
    public WilliamHillPricesManager williamHillPricesManager() {
        return new WilliamHillPricesManager();
    }

    @Bean
    public ViewResolver viewResolver() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Property.getProperty(PropertyKey.SYSTEM_JDBC_DRIVER));
        dataSource.setUrl(Property.getProperty(PropertyKey.SYSTEM_JDBC_URL));
        dataSource.setUsername(Property.getProperty(PropertyKey.SYSTEM_JDBC_USERNAME));
        dataSource.setPassword(Property.getProperty(PropertyKey.SYSTEM_JDBC_PASSWORD));

        return dataSource;
    }

    @Autowired
    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {

        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addAnnotatedClass(EventData.class);
        sessionBuilder.addAnnotatedClass(MarketData.class);
        sessionBuilder.addAnnotatedClass(Prices.class);
        sessionBuilder.addAnnotatedClass(PricesData.class);
        sessionBuilder.addAnnotatedClass(RunnerData.class);
        sessionBuilder.addAnnotatedClass(WhEventParentData.class);
        sessionBuilder.addAnnotatedClass(RaceTimeData.class);
        SessionFactory sessionFactory = sessionBuilder.buildSessionFactory();
        return sessionFactory;
    }

    @Autowired
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    @Bean
    public WhEventParentDao whEeventParentDao() {
        return new HibernateWhEventParentDao();
    }

    @Bean
    public BetfairHelper betfairHelper() {
        return new BetfairHelper();
    }

    @Bean
    public BetfairFootballConverter betfairFootballConverter() {
        return new BetfairFootballConverter();
    }

    @Bean
    public BetfairHorseRacingConverter betfairHorseRacingConverter() {
        return new BetfairHorseRacingConverter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}
