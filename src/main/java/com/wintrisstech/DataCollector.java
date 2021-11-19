package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211111
 * Builds data event id array and calendar date array
 *******************************************************************/

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class DataCollector {
    private static HashMap<String, String> thisWeekTeams = new HashMap<>();
    private static HashMap<String, String> bet365HomeTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365AwayTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365Odds = new HashMap<>();
    private static ArrayList<String> oddsArray = new ArrayList<>();
    private static ArrayList<String> thisWeekMatchupIDlist = new ArrayList<>();
    private static Elements odddsElements;
    private static ArrayList<String> homeAmericanOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeAmericanOddsMap = new HashMap<>();
    private static ArrayList<String> homeDecimalOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeDecimalOddsMap = new HashMap<>();
    private static ArrayList<String> homeFractionalOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeFractionalOddsMap = new HashMap<>();
    private static ArrayList<String> awayAmericanOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayAmericanOddsMap = new HashMap<>();
    private static ArrayList<String> awayDecimalOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayDecimalOddsMap = new HashMap<>();
    private static ArrayList<String> awayFractionalOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayFractionalOddsMap = new HashMap<>();
    private static Iterable<? extends Element> thisWeekMatchupElements;
    private String thisMatchupID;
    private String homeTeamNickname;//e.g. Browns...data-home-team-nickname-search
    private String awayTeamNickname;//e.g Texans...data-away-team-nickname-search
    private static String awayTeamFullName;//e.g. Cleveland...data-home-team-fullname-search
    private static String homeTeamFullName;//e.g Houston...data-home-team-fullname-search
    private String awayTeamCompleteName;//e.g. Kansas City Chiefs
    private static String homeTeamCompleteName;//e.g Houston Texans
    private String gameIdentifier;//e.g 2020 - Houston Texans @ Kansas City Chiefs
    private String awayTeamScore;
    private String homeTeamScore;
    private String gameDate;
    private String awayTeamCity;
    private String homeTeamCity;
    private String thisWeek;
    private String thisSeason;
    private ArrayList<String> thisWeekGameDates = new ArrayList<String>();
    private ArrayList<String> thisGameWeekNumbers = new ArrayList<String>();
    private ArrayList<String> thisWeekHomeTeamScores = new ArrayList<String>();
    private ArrayList<String> thisWeekAwayTeamScores = new ArrayList<String>();
    private ArrayList<String> thisWeekHomeTeams = new ArrayList<String>();
    private ArrayList<String> atsHomes = new ArrayList<String>();
    private ArrayList<String> thisWeekAwayTeams = new ArrayList<String>();
    private HashMap<String, String> gameDatesMap = new HashMap<>();
    private HashMap<String, String> gameIdentifierMap = new HashMap<>();
    private HashMap<String, String> thisWeekHomeTeamsMap = new HashMap<>();
    private HashMap<String, String> thisWeekAwayTeamsMap = new HashMap<>();
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouUndersMap = new HashMap<>();
    private HashMap<String, String> ouOversMap = new HashMap<>();
    private HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> idXref = new HashMap<>();
    private static String[] bet365OddsArray = new String[6];

    public void collectThisWeekMatchups(Elements thisWeekIdElements)//From covers.com website for this week's matchups
    {
        thisWeekMatchupIDlist = new ArrayList<>();
        thisWeekMatchupElements = thisWeekIdElements;
        for (Element e : thisWeekMatchupElements)//Build week matchup IDs array
        {
            homeTeamFullName = e.attr("data-home-team-fullname-search");//e.g. Houston
            homeTeamNickname = e.attr("data-home-team-nickname-search");//e.g. Texans
            homeTeamCity = e.attr("data-home-team-city-search");
            homeTeamCity = cityNameMap.get(homeTeamCity);
            homeTeamCompleteName = homeTeamCity + " " + homeTeamNickname;
            awayTeamFullName = e.attr("data-away-team-fullname-search");//e.g. Dallas
            awayTeamNickname = e.attr("data-away-team-nickname-search");//e.g. Cowboys
            awayTeamCity = e.attr("data-away-team-city-search");
            awayTeamCity = cityNameMap.get(awayTeamCity);
            awayTeamCompleteName = awayTeamCity + " " + awayTeamNickname;
            gameIdentifier = thisSeason + " - " + awayTeamCompleteName + " @ " + homeTeamCompleteName;
            thisMatchupID = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            gameDate = gameDateTime[0];
            awayTeamScore = e.attr("data-away-score");
            thisWeek = e.attr("data-competition-type");
            thisWeekGameDates.add(gameDate);
            gameDatesMap.put(thisMatchupID, gameDate);
            gameIdentifierMap.put(thisMatchupID, gameIdentifier);
            thisWeekHomeTeams.add(homeTeamCompleteName);
            thisWeekAwayTeams.add(awayTeamCompleteName);
            thisWeekHomeTeamsMap.put(thisMatchupID, homeTeamFullName);
            thisWeekAwayTeamsMap.put(thisMatchupID, awayTeamFullName);
            thisWeekHomeTeamScores.add(homeTeamScore);
            thisWeekAwayTeamScores.add((awayTeamScore));
            thisGameWeekNumbers.add(thisWeek);
            thisWeekMatchupIDlist.add(thisMatchupID);
            int i = 0;
            for (Element ee : thisWeekIdElements)
            {
                String dataLinkString = thisWeekIdElements.attr("data-link-id");
                System.out.println(dataLinkString);
                String[] dlsa = dataLinkString.split("/");
//                System.out.println(dlsa[5]);
//                idXref.put("11111", dlsa[5]);
                System.out.println(idXref.keySet());
            }
        }
        for (String s : gameIdentifierMap.keySet()) {
            System.out.print(s);
            System.out.println(" " + gameIdentifierMap.get(s));
        }
    }
    public void collectConsensusData(Elements thisMatchupConsensus, String thisMatchupID) {
        this.thisMatchupID = thisMatchupID;
        String ouOver = null;
        String ouUnder = null;
        String atsHome = null;
        String atsAway = null;
        Elements rightConsensus = thisMatchupConsensus.select(".covers-CoversConsensusDetailsTable-finalWagersright");//Home/Under
        Elements leftConsensus = thisMatchupConsensus.select(".covers-CoversConsensusDetailsTable-finalWagersleft");//Away/Over
        try//To catch missing consensus data due to delayed or cancelled game
        {
            ouUnder = rightConsensus.select("div").get(1).text();
            ouOver = leftConsensus.select("div").get(1).text();
            atsHome = leftConsensus.select("div").get(0).text();
            atsAway = rightConsensus.select("div").get(0).text();
        } catch (Exception e) {
            System.out.println("101 DataCollector, no consensus data for " + gameIdentifier);// ========================> Missing Consensus Data" + e);
        }
        ouOversMap.put(thisMatchupID, ouOver);
        ouUndersMap.put(thisMatchupID, ouUnder);
        atsHomesMap.put(thisMatchupID, atsAway);
        atsAwaysMap.put(thisMatchupID, atsHome);
    }

    public void collectSeasonInfo(Elements nflRandomElements) {
        ArrayList<String> seasonDates = new ArrayList<String>();
        ArrayList<String> seasonCodes = new ArrayList<String>();
        Elements cmg_season_dropdown = nflRandomElements.select("#cmg_season_dropdown");
        Elements options = cmg_season_dropdown.select("Option");
        for (Element e : options) {
            seasonDates.add(e.text());
            seasonCodes.add(e.val());
        }
    }

    public void collectThisSeasonWeeks(Elements nflRandomElements) {
        thisGameWeekNumbers = new ArrayList<String>();
        thisWeekGameDates = new ArrayList<String>();
        Elements cmg_week_filter_dropdown = nflRandomElements.select("#cmg_week_filter_dropdown");
        Elements options = cmg_week_filter_dropdown.select("Option");
        int i = 0;
        for (Element e : options) {
            thisGameWeekNumbers.add(e.text());
            thisWeekGameDates.add(e.val());
            i++;
        }
    }

    public static void collectThisWeekOdds(Elements oddsElements, String dataEventId)
    {
        thisWeekMatchupIDlist = new ArrayList<>();
        Elements table = oddsElements.select("table > tbody > tr:nth-child(1)");
        table = oddsElements;
        String homeAmericanOdds;
        String homeDecinalOdds;
        String homeFractionalOdds;
        String awayAmericanOdds;
        String awayDecimalOdds;
        String awayFractionalOdds;
        String bet365 = table.select(".covers-CoversMatchups-centerAlignHelper, .covers-CoversOdss-oddsTd, .liveOddsCell, [data-book=bet365], bookOdds, awayOdds, .American, .american, span").text();
        //String bet365 = bet365a.select("[data-book=bet365] span").get(0).text();
        awayAmericanOdds = bet365OddsArray[0]; //Away American
//        homeDecinalOdds = bet365Array[1]; //Home Decimal
//        homeFractionalOdds = bet365Array[2]; //Home Fractional
//        awayDecimalOdds = bet365Array[4]; //Away Decimal
//        awayFractionalOdds = bet365Array[5]; //Away Fractional
//        homeAmericanOddsMap.put(dataEventId, homeAmericanOdds);
        // System.out.println("homeAmericanOdds in oddsDataCollector => " + homeAmericanOddsMap.get(dataEventId));
        //System.out.println("homeAway.size() => " + homeAway.ownText());
        int rowCounter = 0;
        for (Element e : oddsElements)
        {
            Element oddsTable = oddsElements.select(".table").get(1);
            Elements oddsRows = oddsTable.select("tr");//Row 2 is first data
            Element selectedRow = oddsRows.get(rowCounter++);
            String oddsData = selectedRow.select("td:eq(9)").text();//Bet365
            bet365OddsArray = oddsData.split(" ");
            for (int i = 0; i < bet365OddsArray.length; i++)
            {
                System.out.println("bet365OddsArray[" + i + "]  " + bet365OddsArray[i]);
            }
            if (bet365OddsArray.length != 1 && bet365OddsArray.length < 21)
            {
                awayAmericanOdds = bet365OddsArray[0];
                homeAmericanOdds = bet365OddsArray[3];
                awayAmericanOddsMap.put(dataEventId, awayAmericanOdds);//Away American Odds
                homeAmericanOddsMap.put(dataEventId, homeAmericanOdds);//Home American Odds
                System.out.println("Away odds for " + awayTeamFullName + " is " + awayAmericanOdds + ", eventID " +  dataEventId);
                System.out.println("Homeodds for " + homeTeamFullName + " is " + homeAmericanOdds + ", eventID " +  dataEventId);
            }
            else
            {
                System.out.println("Missing odds data, bet365OddsArray.length() is: " + bet365OddsArray.length);
            }
        }
    }

    public ArrayList<String> getThisWeekMatchupIDs() {
        return thisWeekMatchupIDlist;
    }

    public HashMap<String, String> getThisWeekHomeTeamsMap() {
        return thisWeekHomeTeamsMap;
    }

    public HashMap<String, String> getThisWeekAwayTeamsMap() {
        return thisWeekAwayTeamsMap;
    }

    public HashMap<String, String> getGameDatesMap() {
        return gameDatesMap;
    }

    public HashMap<String, String> getAtsHomesMap() {
        return atsHomesMap;
    }

    public HashMap<String, String> getAtsAwaysMap() {
        return atsAwaysMap;
    }

    public HashMap<String, String> getOuOversMap() {
        return ouOversMap;
    }

    public HashMap<String, String> getOuUndersMap() {
        return ouUndersMap;
    }

    public HashMap<String, String> getGameIdentifierMap() {
        return gameIdentifierMap;
    }

    public void setThisSeason(String thisSeason) {
        this.thisSeason = thisSeason;
    }

    public String getAwayTeamCompleteName() {
        return awayTeamCompleteName;
    }

    public String getHomeTeamCompleteName() {
        return homeTeamCompleteName;
    }

    public void setCityNameMap(HashMap<String, String> cityNameMap) {
        this.cityNameMap = cityNameMap;
    }
}
