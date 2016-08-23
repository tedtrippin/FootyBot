package com.rob.betBot.exchange;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exception.NoSuchExchangeException;

@Component
public class ExchangeFactory {

    @Autowired
    private Collection<ExchangeCreator> creators;

    public Exchange getExchange(int exchangeId, String username, String password)
        throws LoginFailedException, ExchangeException, NoSuchExchangeException {

        for (ExchangeCreator creator : creators) {
            if (creator.getExchangeInfo().getId() != exchangeId)
                continue;

            return creator.createExchange(username, password);
        }

        throw new NoSuchExchangeException(exchangeId);
    }

    /**
     * Returns info on available exchanges.
     *
     * @return
     */
    public Collection<ExchangeInfo> getExchanges() {

        Collection<ExchangeInfo> infos = new ArrayList<>();
        creators.forEach(ec -> infos.add(ec.getExchangeInfo()));
        return infos;
    }
}
