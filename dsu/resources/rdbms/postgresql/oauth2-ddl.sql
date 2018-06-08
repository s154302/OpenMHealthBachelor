\c omh

CREATE TABLE oauth_access_token (
  token_id          VARCHAR(256),
  token             BYTEA,
  authentication_id VARCHAR(256),
  user_name         VARCHAR(256),
  client_id         VARCHAR(256),
  authentication    BYTEA,
  refresh_token     VARCHAR(256)
);

CREATE TABLE oauth_refresh_token (
  token_id       VARCHAR(256),
  token          BYTEA,
  authentication BYTEA
);

CREATE TABLE oauth_client_details (
  client_id               VARCHAR(256) PRIMARY KEY,
  resource_ids            VARCHAR(256),
  client_secret           VARCHAR(256),
  scope                   VARCHAR(256),
  authorized_grant_types  VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities             VARCHAR(256),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  VARCHAR(4096),
  autoapprove             VARCHAR(256)
);

INSERT INTO oauth_client_details 
( client_id,
   client_secret,
   scope,
   resource_ids,
   authorized_grant_types,
   authorities
)
VALUES (
  'testClient',
  'testClientSecret',
  'read_data_points,write_data_points,delete_data_points',
  'dataPoints',
  'authorization_code,implicit,password,refresh_token',
  'ROLE_CLIENT'
);
