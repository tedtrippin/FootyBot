import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.mvc.model.BetBotCookie;
import com.rob.betBot.util.JsonUtils;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;

public class BotRandomTests {
/*
    @BeforeClass
    public static void init()
        throws IOException {

        Properties properties = new Properties();
        InputStream propertiesStream = BotRandomTests.class.getClassLoader().getResourceAsStream("betbot.properties");
        properties.load(propertiesStream);
        new Property().onLoad(properties);
    }
*/

    @Test
    public void testUi4j() {

        try {

//            String url = "http://sports.williamhill.com/bet/en-gb/betting/t/1517/Copa-Do-Brasil.html";
            String url = "http://sports.williamhill.com/bet/en-gb/betting/e/7918503";
            BrowserEngine webkit = BrowserFactory.getWebKit();
while (true) {
            try (Page page = webkit.navigate(url);) {
                Document document = page.getDocument();
//                Optional<Element> optional = document.query("script:contains('document.ip_list.create_prebuilt_event')");
//                Optional<Element> optional = document.query("script");
//                if (optional == null) {
//                    System.out.println("empty");
//                    return;
//                }
                try {
//                    String bodyHtml = document.getBody().getInnerHTML();

                    String s = document.getBody().getAttribute("onLoad").get();
                    System.out.println(s);

                    if (s.equals("challenge()"))  {
                        System.out.println("CHALLENGED");
//                        document.query("form").get().focus();
//                        Robot robot = new Robot();
//                        robot.keyPress(KeyEvent.VK_ENTER);
                        page.executeScript("document.forms[0].submit()");
                        System.out.println( document.getBody().getInnerHTML() );

                    }
                } catch (NoSuchElementException ex) {
                    System.out.println("No body onload challenge");
                }


//                List<Element> scripts = document.queryAll("script");
//                for (Element script : scripts) {
//                    if (script.getInnerHTML().contains("document.ip_list.create_prebuilt_event("))
//                        System.out.println("found it");
//                }
            }
}

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("done");
    }

    @Test
    public void test2() {

        try {

            //String s = "{\"name\":\"Default\",\"filters\":[{\"properties\":[{\"name\":\"Age\",\"displayName\":\"Age\",\"type\":\"NUMBER\",\"value\":\"7\"}],\"id\":\"1\",\"name\":\"Remove runners over a certain age\",\"description\":\"Removes runner over a given weight.\",\"timesRun\":0}],\"betPlacers\":[]}";
            String s = "{"
                + "\"name\":\"Default\","
                + "\"filters\":[],"
                + "\"betPlacers\":[{\"betsToRedo\":[],\"runnerAmountLayedMap\":{},"
                + "\"properties\":["
                +   "{\"name\":\"LayAmount\",\"displayName\":\"Lay amount\",\"type\":\"NUMBER\",\"value\":\"2\"},"
                +   "{\"name\":\"Percentage\",\"displayName\":\"Percentage of price\",\"type\":\"NUMBER\",\"value\":\"90\"}"
                + "],\"id\":\"2\",\"name\":\"W"
                + "inner percentage layer bette\",\"description\":\"Lays off at a percentage of the exchange odds. Eg. if percen"
                + "tage\u003d90 , lay amount\u003d100 and price\u003d10 then places lay bet of £100 at price of 9. Bet is updated "
                + "when odds change\",\"timesRun\":0}]}";

            BetBotCookie cookie = JsonUtils.readValue(s, BetBotCookie.class);

            Bot bot = new Bot(cookie.getName());

            ModuleFactory moduleFactory = new ModuleFactory();

            for (JsonObject filterJson : cookie.getFilters()) {
                bot.addFilter(moduleFactory.createFilter(filterJson));
            }

            for (JsonObject betPlacerJson : cookie.getBetPlacers()) {
                bot.addBetPlacer(moduleFactory.createBetPlacer(betPlacerJson));
            }

            System.out.println("done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testOffset()
        throws ParseException {

        String offset = "  0 00:26:44".trim();
        Calendar date = Calendar.getInstance();
        try {
            String[] offsets = offset.split("[ :]");


            System.out.println(offsets[0]);
            System.out.println(offsets[1]);
            System.out.println(offsets[2]);


            date.add(Calendar.DAY_OF_MONTH, Integer.parseInt(offsets[0]));
            date.add(Calendar.HOUR, Integer.parseInt(offsets[1]));
            date.add(Calendar.MINUTE, Integer.parseInt(offsets[2]));
        } catch (NumberFormatException ex) {
            System.out.println("Unexpected offset format[" + offset + "]");
        }

        System.out.println(new Date(date.getTimeInMillis()));
    }

    @Test
    public void test4() {

        String name = "chelseavliverpool";
        String altName1 = "chelseavliverpool";
        String altName2 = "chelseavliverpool";
        String altName3 = "chelsea-liverpool";
        String altName4 = "chelsea-liverpool";
        String altName5 = "chelsea-liverpool";
        String altName6 = "liverpool-chelsea";

        System.out.println(StringUtils.getLevenshteinDistance(name, altName1));
        System.out.println(StringUtils.getLevenshteinDistance(name, altName2));
        System.out.println(StringUtils.getLevenshteinDistance(name, altName3));
        System.out.println(StringUtils.getLevenshteinDistance(name, altName4));
        System.out.println(StringUtils.getLevenshteinDistance(name, altName5));
        System.out.println(StringUtils.getLevenshteinDistance(name, altName6));

        System.out.println("-----------------------------");

        System.out.println(StringUtils.getJaroWinklerDistance(name, altName1));
        System.out.println(StringUtils.getJaroWinklerDistance(name, altName2));
        System.out.println(StringUtils.getJaroWinklerDistance(name, altName3));
        System.out.println(StringUtils.getJaroWinklerDistance(name, altName4));
        System.out.println(StringUtils.getJaroWinklerDistance(name, altName5));
        System.out.println(StringUtils.getJaroWinklerDistance(name, altName6));
    }

    @Test
    public void test5() {

//        String description = "Distance: 1m7f 207yds &nbsp;|&nbsp;";
//        String description2 = "Distance: 7f 207yds &nbsp;|&nbsp;";
//        String description3 = "Distance: 7m 207yds &nbsp;|&nbsp;";
//        String description4 = "Distance: 2m 140yds  | ";
        String description5 = "Distance: 2m  | ";

        Pattern pattern = Pattern.compile("([1-9]+m)([0-9]+f)?( [0-9]+yds)?");

//        tryMatch(pattern, description);
//        tryMatch(pattern, description2);
//        tryMatch(pattern, description3);
//        tryMatch(pattern, description4);
        tryMatch(pattern, description5);
    }

    private void tryMatch(Pattern pattern, String description) {

        System.out.println("\n" + description);
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            for (int idx = 1; idx <= matcher.groupCount(); idx++) {
                System.out.println(matcher.group(idx));
            }
        } else {
            System.out.println("no match");
        }
    }

    @Test
    public void testCleanTime() {

        Pattern TIME_PATTERN = Pattern.compile("([0-9]+)m ([0-9]+).([0-9]+)s");

        Matcher matcher = TIME_PATTERN.matcher("4m 1.00s");
        System.out.println(matcher.matches());

        for (int idx = 0; idx <= matcher.groupCount(); idx++) {
            String s = matcher.group(idx);
            System.out.println(idx + ": " + s);
        }
    }

    @Test
    public void testCleanDistance() {

        String s = "Distance: 2m 140yds  | ";
        System.out.println("from: " + s);
        System.out.println("to: " + cleanDistance(s));
    }

    private final static Pattern DISTANCE_PATTERN = Pattern.compile("([1-9]+m)([0-9]+f)?( [0-9]+yds)");
    private String cleanDistance(String distance) {

        if (Strings.isNullOrEmpty(distance))
            return null;

        Matcher matcher = DISTANCE_PATTERN.matcher(distance);
System.out.println(matcher.find());

        StringBuilder sb = new StringBuilder();
        for (int idx = 1; idx <= matcher.groupCount(); idx++) {
            String s = matcher.group(idx);
            if (s != null)
                sb.append(s);
        }

        return sb.toString();
    }

}

