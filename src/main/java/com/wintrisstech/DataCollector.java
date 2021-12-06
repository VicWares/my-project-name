package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211205
 * Builds data event id array and calendar date array
 *******************************************************************/

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class DataCollector
{
    private static HashMap<String, String> thisWeekTeams = new HashMap<>();
    private static HashMap<String, String> bet365HomeTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365AwayTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365Odds = new HashMap<>();
    private static ArrayList<String> oddsArray = new ArrayList<>();
    private static ArrayList<String> thisWeekMatchuplist = new ArrayList<>();
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
    private static int rowCounter;
    private String thisMatchup;
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

    public void collectThisWeekMatchups(Elements thisWeekElements)//From covers.com website for this week's matchups
    {
        for (Element e : thisWeekElements)//Build week matchup IDs array
        {
            homeTeamFullName = e.attr("data-home-team-fullname-search");//e.g. Houston...correcting for different city/name usage
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
            thisMatchup = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            gameDate = gameDateTime[0];
            awayTeamScore = e.attr("data-away-score");
            thisWeek = e.attr("data-competition-type");
            thisWeekGameDates.add(gameDate);
            gameDatesMap.put(thisMatchup, gameDate);
            gameIdentifierMap.put(thisMatchup, gameIdentifier);
            thisWeekHomeTeams.add(homeTeamCompleteName);
            thisWeekAwayTeams.add(awayTeamCompleteName);
            thisWeekHomeTeamsMap.put(thisMatchup, homeTeamFullName);
            thisWeekAwayTeamsMap.put(thisMatchup, awayTeamFullName);
            thisWeekHomeTeamScores.add(homeTeamScore);
            thisWeekAwayTeamScores.add((awayTeamScore));
            thisGameWeekNumbers.add(thisWeek);
            thisWeekMatchuplist.add(thisMatchup);
            String[] dataLinkElementsSplit = null;
            dataLinkElementsSplit = e.toString().split("/");
        }
    }

    public void collectConsensusData(Elements thisMatchupConsensus, String thisMatchupID)
    {
        this.thisMatchup = thisMatchupID;
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
        }
        catch (Exception e)
        {
            System.out.println("DC121 DataCollector, no consensus data for " + gameIdentifier);
        }
        ouOversMap.put(thisMatchupID, ouOver);
        ouUndersMap.put(thisMatchupID, ouUnder);
        atsHomesMap.put(thisMatchupID, atsAway);
        atsAwaysMap.put(thisMatchupID, atsHome);
    }

    public static void collectThisWeekOdds(Elements oddsElements)
    {
        int i = 0;
        thisWeekMatchuplist = new ArrayList<>();
        Elements table = oddsElements.select("table > tbody > tr:nth-child(9)");//Column 9 is bet365
       try
       {
           while(true)
           {
               Elements tdsInSecondRow = oddsElements.select("table tr:eq(" + i + ") > td");
               Elements shortNames = tdsInSecondRow.select(".__shortname");
               System.out.print(shortNames.get(0).text() + " at ");
               System.out.println(shortNames.get(1).text());
               i++;
           }
       }
       catch (Exception e)
       {
           System.out.println("DC152 End of odds collecting.  " + i + " teams played this week");
       }
        //while (true)
//        {
            //System.out.println("DC141 Odds for " + table.select(".__shortname").get(0).text() + " vs " + table.select(".__shortname").get(1).text());
//        }
//        String homeAmericanOdds;
//        String homeDecinalOdds;
//        String homeFractionalOdds;
//        String awayAmericanOdds;
//        String awayDecimalOdds;
//        String awayFractionalOdds;
        //String bet365 = table.select(".covers-CoversMatchups-centerAlignHelper, .covers-CoversOdss-oddsTd, .liveOddsCell, [data-book=bet365], bookOdds, awayOdds, .American, .american, span").text();
        //String bet365 = bet365a.select("[data-book=bet365] span").get(0).text();
//        awayAmericanOdds = bet365OddsArray[0]; //Away American
//        homeDecinalOdds = bet365Array[1]; //Home Decimal
//        homeFractionalOdds = bet365Array[2]; //Home Fractional
//        awayDecimalOdds = bet365Array[4]; //Away Decimal
//        awayFractionalOdds = bet365Array[5]; //Away Fractional
//        homeAmericanOddsMap.put(dataEventId, homeAmericanOdds);
        // System.out.println("homeAmericanOdds in oddsDataCollector => " + homeAmericanOddsMap.get(dataEventId));
        //System.out.println("homeAway.size() => " + homeAway.ownText());
//        Element oddsTable = oddsElements.select(".table").get(1);
//        Elements oddsRows = oddsTable.select("tr");//Row 2 is first data
//        for (int rowcounter = 0; rowcounter < oddsRows.size(); rowcounter++)
//        {
//            Element selectedRow = oddsRows.get(rowCounter);
//            System.out.println("&&& " + selectedRow);
//            String oddsData = selectedRow.select("td:eq(9)").text();//Bet365
//            bet365OddsArray = oddsData.split(" ");
//            awayAmericanOdds = bet365OddsArray[0];
//            homeAmericanOdds = "999";//bet365OddsArray[3];
//            awayAmericanOddsMap.put(dataEventId, awayAmericanOdds);//Away American Odds
//            homeAmericanOddsMap.put(dataEventId, homeAmericanOdds);//Home American Odds
//            System.out.println("DC174 Away odds for " + awayTeamFullName + " is " + awayAmericanOdds + ", eventID " + dataEventId);
//            System.out.println("DC175 Home odds for " + homeTeamFullName + " is " + homeAmericanOdds + ", eventID " + dataEventId);
//        }
    }

    public ArrayList<String> getMatchups()
    {
        return thisWeekMatchuplist;
    }

    public HashMap<String, String> getThisWeekHomeTeamsMap()
    {
        return thisWeekHomeTeamsMap;
    }

    public HashMap<String, String> getThisWeekAwayTeamsMap()
    {
        return thisWeekAwayTeamsMap;
    }

    public HashMap<String, String> getGameDatesMap()
    {
        return gameDatesMap;
    }

    public HashMap<String, String> getAtsHomesMap()
    {
        return atsHomesMap;
    }

    public HashMap<String, String> getAtsAwaysMap()
    {
        return atsAwaysMap;
    }

    public HashMap<String, String> getOuOversMap()
    {
        return ouOversMap;
    }

    public HashMap<String, String> getOuUndersMap()
    {
        return ouUndersMap;
    }

    public HashMap<String, String> getGameIdentifierMap()
    {
        return gameIdentifierMap;
    }

    public void setThisSeason(String thisSeason)
    {
        this.thisSeason = thisSeason;
    }

    public String getAwayTeamCompleteName()
    {
        return awayTeamCompleteName;
    }

    public String getHomeTeamCompleteName()
    {
        return homeTeamCompleteName;
    }

    public void setCityNameMap(HashMap<String, String> cityNameMap)
    {
        this.cityNameMap = cityNameMap;
    }
}
