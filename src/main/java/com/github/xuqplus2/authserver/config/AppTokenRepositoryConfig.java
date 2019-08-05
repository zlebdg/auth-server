package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.config.kz.AppJdbcTokenRepositoryImpl;
import com.github.xuqplus2.authserver.config.kz.AppRememberMeServices;
import com.github.xuqplus2.authserver.config.kz.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
public class AppTokenRepositoryConfig {

    @Bean
    @Autowired
    public AppJdbcTokenRepositoryImpl repository(DataSource dataSource) {
        AppJdbcTokenRepositoryImpl repository = new AppJdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        repository.setCreateTableOnStartup(false);
        return repository;
    }

    @Bean
    @Autowired
    public AppRememberMeServices rememberMeServices(PersistentTokenRepository repository, AppUserDetailsService userDetailsService) {
        AppRememberMeServices appRememberMeServices = new AppRememberMeServices(UUID.randomUUID().toString(), userDetailsService, repository);
        return appRememberMeServices;
    }
}
