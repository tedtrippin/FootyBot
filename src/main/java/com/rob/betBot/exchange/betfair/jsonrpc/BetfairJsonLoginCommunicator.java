package com.rob.betBot.exchange.betfair.jsonrpc;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;

public class BetfairJsonLoginCommunicator {

    private static Logger log = LogManager.getLogger(BetfairJsonLoginCommunicator.class);

    private String applicationKey = "";
    private String sessionToken = "";
    private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    private long sessionExpiryTimeout = 1000 * 60 * 60 * 10; // Timeout is 12 hours, we'll set to 10
    private long nextKeepAlive;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    public BetfairJsonLoginCommunicator() {

        applicationKey = Property.getProperty(PropertyKey.BETFAIR_APPLICATION_KEY);
        headers.putSingle("X-Application", applicationKey);
        headers.putSingle("Accept", MediaType.APPLICATION_JSON);
    }

    /**
     * Logons to Betfair and returns a session token.
     *
     * @param username
     * @param password
     * @return
     * @throws ExchangeException
     * @throws LoginFailedException
     */
    public String login(String username, String password)
        throws ExchangeException, LoginFailedException {

        log.info("[" + username + "] logging in");

        Form loginForm = new Form();
        loginForm.param(USERNAME, username);
        loginForm.param(PASSWORD, password);

        String url = getBetfairLoginUrl();
        if (!url.endsWith("/"))
            url += '/';
        url += "login";

        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(url)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(headers)
                .buildPost(Entity.form(loginForm))
                .invoke();

            LoginResponse loginResponse = response.readEntity(LoginResponse.class);
            if (!loginResponse.getStatus().equals("SUCCESS"))
                throw new LoginFailedException("Login failed[" + loginResponse + "]");

            sessionToken = loginResponse.getToken();
            headers.putSingle("X-Authentication", sessionToken);
            nextKeepAlive = System.currentTimeMillis() + sessionExpiryTimeout;

            return sessionToken;

        } catch (ProcessingException ex) {
            throw new ExchangeException("Error during login", ex);
        }
    }

    public void keepAlive()
        throws ExchangeException {

        if (System.currentTimeMillis() < nextKeepAlive)
            return;

        log.debug("Doing keep alive");

        String url = getBetfairLoginUrl();
        if (!url.endsWith("/"))
            url += '/';
        url += "keepAlive";

        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(url)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(headers)
                .buildGet()
                .invoke();

            LoginResponse loginResponse = response.readEntity(LoginResponse.class);
            if (loginResponse.getStatus() != "SUCCESS")
                throw new ExchangeException("Keep alive failed[" + loginResponse + "]");

            nextKeepAlive = System.currentTimeMillis() + sessionExpiryTimeout;

        } catch (ProcessingException ex) {
            throw new ExchangeException("Error during keepAlive", ex);
        }
    }

    public String getSessionToken() {
        return sessionToken;
    }

    private static String getBetfairLoginUrl() {
        return Property.getProperty(PropertyKey.BETFAIR_JSON_LOGIN_URL);
    }

    static class LoginResponse {

        private String token;
        private String product;
        private String status;
        private String error;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                .append(",token=").append(token)
                .append(",product=").append(product)
                .append(",status=").append(status)
                .append(",error=").append(error).toString();
        }
    }
}
