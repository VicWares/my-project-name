package com.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211010
 * Builds data event id array and calendar date array
 *******************************************************************/
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
public class DataCollector
{
    private String thisMatchupID;
    private String homeTeamNickname;//e.g. Browns...data-home-team-nickname-search
    private String awayTeamNickname;//e.g Texans...data-away-team-nickname-search
    private String awayTeamFullName;//e.g. Cleveland...data-home-team-fullname-search
    private String homeTeamFullName;//e.g Houston...data-home-team-fullname-search
    private String awayTeamCompleteName;//e.g. Kansas City Chiefs
    private String homeTeamCompleteName;//e.g Houston Texans
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
    private ArrayList<String> thisWeekMatchupIDs;
    private HashMap<String, String> gameDatesMap = new HashMap<>();
    private HashMap<String, String> gameIdentifierMap= new HashMap<>();
    private HashMap<String, String> thisWeekHomeTeamsMap = new HashMap<>();
    private HashMap<String, String> thisWeekAwayTeamsMap = new HashMap<>();
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouUndersMap = new HashMap<>();
    private HashMap<String, String> ouOversMap = new HashMap<>();
    private HashMap<String,String> cityNameMap = new HashMap<>();
    private Elements thisWeekMatchupIdElements;
    public void collectThisWeekMatchups(Elements thisWeekElements)//From covers.com sebsite for this week's matchups
    {
        thisWeekMatchupIDs = new ArrayList<>();
        thisWeekMatchupIdElements = thisWeekElements.select(".cmg_matchup_game_box");
        for (Element e : thisWeekMatchupIdElements)//Build week matchup IDs array
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
            gameIdentifier =  thisSeason + " - " + awayTeamCompleteName + " @ " + homeTeamCompleteName;
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
            thisWeekMatchupIDs.add(thisMatchupID);
        }
    }


    public void collectConsensusData(Elements thisMatchupConsensus, String thisMatchupID)
    {
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
            System.out.println("101 DataCollector, no consensus data for " + gameIdentifier);// ========================> Missing Consensus Data" + e);
        }
        ouOversMap.put(thisMatchupID,ouOver);
        ouUndersMap.put(thisMatchupID, ouUnder);
        atsHomesMap.put(thisMatchupID, atsAway);
        atsAwaysMap.put(thisMatchupID, atsHome);
    }
    public void collectSeasonInfo(Elements nflRandomElements)
    {
        ArrayList<String> seasonDates = new ArrayList<String>();
        ArrayList<String> seasonCodes = new ArrayList<String>();
        Elements cmg_season_dropdown = nflRandomElements.select("#cmg_season_dropdown");
        Elements options = cmg_season_dropdown.select("Option");
        for (Element e : options)
        {
            seasonDates.add(e.text());
            seasonCodes.add(e.val());
        }
    }
    public void collectThisSeasonWeeks(Elements nflRandomElements)
    {
        thisGameWeekNumbers = new ArrayList<String>();
        thisWeekGameDates = new ArrayList<String>();
        Elements cmg_week_filter_dropdown = nflRandomElements.select("#cmg_week_filter_dropdown");
        Elements options = cmg_week_filter_dropdown.select("Option");
        int i = 0;
        for (Element e : options)
        {
            thisGameWeekNumbers.add(e.text());
            thisWeekGameDates.add(e.val());
            i++;
        }
    }
    public ArrayList<String> getThisWeekMatchupIDs()
    {
        return thisWeekMatchupIDs;
    }
    public HashMap<String, String> getThisWeekHomeTeamsMap(){return thisWeekHomeTeamsMap;}
    public HashMap<String, String> getThisWeekAwayTeamsMap(){return thisWeekAwayTeamsMap;}
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
    public void setThisSeason(String thisSeason){ this.thisSeason = thisSeason; }
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
