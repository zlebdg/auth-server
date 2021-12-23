package com.github.xuqplus2.authserver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${spring.jpa.hibernate.ddl-auto:none}")
    String ddl;
    @Value("${project.blog.client.pre-established-redirect-uri:}")
    String blogLoginUrl;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String OAUTH_CLIENT_DETAILS =
            "CREATE TABLE IF NOT EXISTS `oauth_client_details`" +
                    "(" +
                    "    `client_id`               VARCHAR(128)  NOT NULL," +
                    "    `resource_ids`            VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `client_secret`           VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `scope`                   VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `authorized_grant_types`  VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `web_server_redirect_uri` VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `authorities`             VARCHAR(256)  NULL DEFAULT NULL," +
                    "    `access_token_validity`   INT(11)       NULL DEFAULT NULL," +
                    "    `refresh_token_validity`  INT(11)       NULL DEFAULT NULL," +
                    "    `additional_information`  VARCHAR(4096) NULL DEFAULT NULL," +
                    "    `autoapprove`             VARCHAR(256)  NULL DEFAULT NULL," +
                    "    PRIMARY KEY (`client_id`)" +
                    ")" +
                    "    ENGINE = InnoDB" +
                    "    DEFAULT CHARACTER SET = utf8";

    private static final String OAUTH_ACCESS_TOKEN =
            "CREATE TABLE IF NOT EXISTS `oauth_access_token`" +
                    "(" +
                    "    `token_id`          VARCHAR(256) NULL DEFAULT NULL," +
                    "    `token`             BLOB         NULL DEFAULT NULL," +
                    "    `authentication_id` VARCHAR(128) NOT NULL," +
                    "    `user_name`         VARCHAR(256) NULL DEFAULT NULL," +
                    "    `client_id`         VARCHAR(256) NULL DEFAULT NULL," +
                    "    `authentication`    BLOB         NULL DEFAULT NULL," +
                    "    `refresh_token`     VARCHAR(256) NULL DEFAULT NULL," +
                    "    PRIMARY KEY (`authentication_id`)" +
                    ")" +
                    "    ENGINE = InnoDB" +
                    "    DEFAULT CHARACTER SET = utf8";

    private static final String OAUTH_APPROVALS =
            "CREATE TABLE IF NOT EXISTS `oauth_approvals`" +
                    "(" +
                    "    `userId`         VARCHAR(256) NULL DEFAULT NULL," +
                    "    `clientId`       VARCHAR(256) NULL DEFAULT NULL," +
                    "    `scope`          VARCHAR(256) NULL DEFAULT NULL," +
                    "    `status`         VARCHAR(10)  NULL DEFAULT NULL," +
                    "    `expiresAt`      DATETIME     NULL DEFAULT NULL," +
                    "    `lastModifiedAt` DATETIME     NULL DEFAULT NULL" +
                    ")" +
                    "    ENGINE = InnoDB" +
                    "    DEFAULT CHARACTER SET = utf8";

    private static final String OAUTH_CODE =
            "CREATE TABLE IF NOT EXISTS `oauth_code`" +
                    "(" +
                    "    `code`           VARCHAR(256) NULL DEFAULT NULL," +
                    "    `authentication` BLOB         NULL DEFAULT NULL" +
                    ")" +
                    "    ENGINE = InnoDB" +
                    "    DEFAULT CHARACTER SET = utf8";

    private static final String OAUTH_REFRESH_TOKEN =
            "CREATE TABLE IF NOT EXISTS `oauth_refresh_token`" +
                    "(" +
                    "    `token_id`       VARCHAR(256) NULL DEFAULT NULL," +
                    "    `token`          BLOB         NULL DEFAULT NULL," +
                    "    `authentication` BLOB         NULL DEFAULT NULL" +
                    ")" +
                    "    ENGINE = InnoDB" +
                    "    DEFAULT CHARACTER SET = utf8";

    private static final String PERSISTENT_LOGINS =
            "CREATE TABLE IF NOT EXISTS `persistent_logins`" +
                    "(" +
                    "    `username`  varchar(64) NOT NULL," +
                    "    `series`    varchar(64) NOT NULL," +
                    "    `token`     varchar(64) NOT NULL," +
                    "    `last_used` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "    PRIMARY KEY (`series`)" +
                    ") ENGINE = InnoDB" +
                    "  DEFAULT CHARSET = utf8";

    private static final String CLIENT_URLS_TEMPLATE = String.join(",",
            "%s",
            "http://blog.loc:20000/blog/login");
    private static final String CLIENT_TEMPLATE =
            "INSERT INTO oauth_client_details (client_secret, resource_ids, scope, authorized_grant_types," +
            "    web_server_redirect_uri, authorities, access_token_validity," +
            "    refresh_token_validity, additional_information, autoapprove, client_id)" +
            "VALUES ('{noop}secret', 'resourceId', 'aaa,bbb,ccc,ddd', 'authorization_code,refresh_token,implicit'," +
            "   '%s', null, null," +
            "   null, '{}', 'aaa,bbb,ccc,ddd', 'client')";

    public static final String[] DDL = {OAUTH_CLIENT_DETAILS, OAUTH_ACCESS_TOKEN, OAUTH_APPROVALS, OAUTH_CODE, OAUTH_REFRESH_TOKEN, PERSISTENT_LOGINS};
    public static final String[] DML = {"delete from oauth_client_details where client_id = 'client'", null};

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        /* 创建一些非jpa的表, spring security用到 */
        if (!"none".equalsIgnoreCase(ddl)) {
            for (String sql : DDL) {
                jdbcTemplate.execute(sql);
            }
        }
        DML[1] = String.format(CLIENT_TEMPLATE, String.format(CLIENT_URLS_TEMPLATE, blogLoginUrl));
        for (String sql : DML) {
            jdbcTemplate.execute(sql);
        }
    }
}
