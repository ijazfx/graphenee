CREATE TABLE gx_authentication (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    name character varying(255),
    description character varying(255),
    auth_type character varying(255),
    username character varying(255),
    password character varying(255),
    bearer_token character varying(255),
    auth_url character varying(255),
    request_type character varying(255),
    request_template text,
    oid_namespace integer not null,
    foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

CREATE TABLE gx_entity_callback (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    name character varying(255),
    description character varying(255),
    entity_name character varying(255),
    event_type character varying(255),
    attribute_set character varying(255),
    callback_url character varying(255),
    request_template text,
    oid_authentication integer,
    oid_namespace integer not null,
    foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
    foreign key (oid_authentication) references gx_authentication(oid) on delete set null on update cascade
);
