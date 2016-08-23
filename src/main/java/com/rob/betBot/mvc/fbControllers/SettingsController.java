package com.rob.betBot.mvc.fbControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.betPlacers.PercentageCorrectScoreLayPlacer;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.bots.modules.ModuleProperty;
import com.rob.betBot.bots.modules.PropertyNames;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.mvc.BotCookie;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.FbBotSettings;

@Controller("FbSettingsController")
@RequestMapping(value = "/fb/settings")
public class SettingsController extends BaseController {

    @ModelAttribute("botSettings")
    public FbBotSettings getBotSettings(@CookieValue(value = ModelKeys.BOT) String botString) {

        try {
            Bot bot = cookieToBot(botString);
            PercentageCorrectScoreLayPlacer placer = null;
            for (BetPlacer bp : bot.getBetPlacers()) {
                if (!(bp instanceof PercentageCorrectScoreLayPlacer))
                    continue;

                placer = (PercentageCorrectScoreLayPlacer) bp;
                break;
            }

            FbBotSettings botSettings = new FbBotSettings();
            for (ModuleProperty p : placer.getProperties()) {
                if (p.getName().equals(PropertyNames.LAY_AMOUNT))
                    botSettings.setLayAmount(p.getValue());
                else if (p.getName().equals(PropertyNames.MAX_PRICE))
                    botSettings.setMaxPrice(p.getValue());
                else if (p.getName().equals(PropertyNames.PERCENTAGE))
                    botSettings.setPercentage(p.getValue());
            }

            return botSettings;

        } catch (InvalidBotException ex) {
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSettings(@CookieValue(value = ModelKeys.BOT, required = false) String botString,
        HttpServletResponse response) {

        return "footybot/settings";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postSettings(@ModelAttribute("botSettings") FbBotSettings settings, HttpServletRequest request,
        HttpServletResponse response) {

        PercentageCorrectScoreLayPlacer placer = new PercentageCorrectScoreLayPlacer();
        for (ModuleProperty p : placer.getProperties()) {
            if (p.getName().equals(PropertyNames.LAY_AMOUNT))
                p.setValue(settings.getLayAmount());
            else if (p.getName().equals(PropertyNames.MAX_PRICE))
                p.setValue(settings.getMaxPrice());
            else if (p.getName().equals(PropertyNames.PERCENTAGE))
                p.setValue(settings.getPercentage());
        }

        Bot bot = new Bot();
        bot.addBetPlacer(placer);
        String json = new ModuleFactory().toJsonString(bot);
        response.addCookie(new BotCookie(json));

        return "footybot/settings";
    }
}
