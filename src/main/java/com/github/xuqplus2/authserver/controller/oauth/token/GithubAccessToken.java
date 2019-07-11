package com.github.xuqplus2.authserver.controller.oauth.token;

import lombok.Data;

@Data
public class GithubAccessToken {

  private String access_token;
  private String token_type;
  private String scope;
}
