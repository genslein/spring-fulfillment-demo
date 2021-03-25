package com.demo.fulfillment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FulfillmentEnvProperties {
    @Value("${DATABASE_CONN_URL}")
    public String databaseConnectionString;

    @Value("${POSTGRES_USER}")
    public String databaseUser;

    @Value("${POSTGRES_PW}")
    public String databasePassword;
}
