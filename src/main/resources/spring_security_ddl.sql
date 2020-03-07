## auth client
CREATE TABLE IF NOT EXISTS `test_auth_client`.`oauth_client_token`
(
    `token_id`          VARCHAR(256) NULL DEFAULT NULL,
    `token`             BLOB         NULL DEFAULT NULL,
    `authentication_id` VARCHAR(128) NOT NULL,
    `user_name`         VARCHAR(256) NULL DEFAULT NULL,
    `client_id`         VARCHAR(256) NULL DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

## auth server
CREATE TABLE IF NOT EXISTS `test_auth_server`.`clientdetails`
(
    `appId`                  VARCHAR(128)  NOT NULL,
    `resourceIds`            VARCHAR(256)  NULL DEFAULT NULL,
    `appSecret`              VARCHAR(256)  NULL DEFAULT NULL,
    `scope`                  VARCHAR(256)  NULL DEFAULT NULL,
    `grantTypes`             VARCHAR(256)  NULL DEFAULT NULL,
    `redirectUrl`            VARCHAR(256)  NULL DEFAULT NULL,
    `authorities`            VARCHAR(256)  NULL DEFAULT NULL,
    `access_token_validity`  INT(11)       NULL DEFAULT NULL,
    `refresh_token_validity` INT(11)       NULL DEFAULT NULL,
    `additionalInformation`  VARCHAR(4096) NULL DEFAULT NULL,
    `autoApproveScopes`      VARCHAR(256)  NULL DEFAULT NULL,
    PRIMARY KEY (`appId`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `test_auth_server`.`oauth_client_details`
(
    `client_id`               VARCHAR(128)  NOT NULL,
    `resource_ids`            VARCHAR(256)  NULL DEFAULT NULL,
    `client_secret`           VARCHAR(256)  NULL DEFAULT NULL,
    `scope`                   VARCHAR(256)  NULL DEFAULT NULL,
    `authorized_grant_types`  VARCHAR(256)  NULL DEFAULT NULL,
    `web_server_redirect_uri` VARCHAR(256)  NULL DEFAULT NULL,
    `authorities`             VARCHAR(256)  NULL DEFAULT NULL,
    `access_token_validity`   INT(11)       NULL DEFAULT NULL,
    `refresh_token_validity`  INT(11)       NULL DEFAULT NULL,
    `additional_information`  VARCHAR(4096) NULL DEFAULT NULL,
    `autoapprove`             VARCHAR(256)  NULL DEFAULT NULL,
    PRIMARY KEY (`client_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `test_auth_server`.`oauth_access_token`
(
    `token_id`          VARCHAR(256) NULL DEFAULT NULL,
    `token`             BLOB         NULL DEFAULT NULL,
    `authentication_id` VARCHAR(128) NOT NULL,
    `user_name`         VARCHAR(256) NULL DEFAULT NULL,
    `client_id`         VARCHAR(256) NULL DEFAULT NULL,
    `authentication`    BLOB         NULL DEFAULT NULL,
    `refresh_token`     VARCHAR(256) NULL DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `test_auth_server`.`oauth_approvals`
(
    `userId`         VARCHAR(256) NULL DEFAULT NULL,
    `clientId`       VARCHAR(256) NULL DEFAULT NULL,
    `scope`          VARCHAR(256) NULL DEFAULT NULL,
    `status`         VARCHAR(10)  NULL DEFAULT NULL,
    `expiresAt`      DATETIME     NULL DEFAULT NULL,
    `lastModifiedAt` DATETIME     NULL DEFAULT NULL
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `test_auth_server`.`oauth_code`
(
    `code`           VARCHAR(256) NULL DEFAULT NULL,
    `authentication` BLOB         NULL DEFAULT NULL
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `test_auth_server`.`oauth_refresh_token`
(
    `token_id`       VARCHAR(256) NULL DEFAULT NULL,
    `token`          BLOB         NULL DEFAULT NULL,
    `authentication` BLOB         NULL DEFAULT NULL
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

## remember-me token
CREATE TABLE `persistent_logins`
(
    `username`  varchar(64) NOT NULL,
    `series`    varchar(64) NOT NULL,
    `token`     varchar(64) NOT NULL,
    `last_used` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`series`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 新建一个client
DELETE
FROM oauth_client_details
WHERE client_id = 'client';

INSERT INTO oauth_client_details (client_secret, resource_ids, scope, authorized_grant_types,
                                  web_server_redirect_uri, authorities, access_token_validity,
                                  refresh_token_validity, additional_information, autoapprove, client_id)
VALUES ('{noop}secret', 'resourceId', 'aaa,bbb,ccc,ddd', 'authorization_code,refresh_token,implicit',
        'https://blog.java8.xyz:444/login,http://blog.loc:20000/login,', '', null,
        null, '{}', 'aaa,bbb,ccc,ddd', 'client');
