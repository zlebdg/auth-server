package com.github.xuqplus2.authserver.config.kz;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

public class AppJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

    public void deleteToken(String series) {
        getJdbcTemplate().update("delete from persistent_logins where series = ?", series);
    }
}
