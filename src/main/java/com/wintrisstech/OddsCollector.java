package com.wintrisstech;
/********************************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211031
 * Reads Bet365 Odds from Covers...https://www.covers.com/sport/football/nfl/odds
 ********************************************************************************/
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
public class OddsCollector {
    private static HashMap<String, String> bet365HomeTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365AwayTeamOdds = new HashMap<>();
    private static ArrayList<String> oddsArray = new ArrayList<>();

    public static void collectThisWeekOdds(String dataEventId, Elements oddsElements, Document oddsDoc)
    {
        Element table = oddsDoc.select("table").get(1); //select the second table...MoneyLine?
       // Elements bet365 = table.select(".covers-CoversOdss-odddsTd, .liveOddsCell, .covers-u-desktopHide, .bookOdds, .awayOdds, .American, .american");
        String bet365 = table.select("td, .covers-CoversMatchups-centerAlignHelper covers-CoversOdss-oddsTd, liveOddsCell, bet365, awayOdds, American, american, span").get(4).text();
        System.out.println("$$$$$" + bet365);//Home: American, decimal, fractional/Away: American, decimal, fractional
//        for (int i = 0; i < bet365.size(); i++)
//        {
//            Element answerElement = bet365.get(i);
//            System.out.println("......................answers Element (" + i + " => " + answerElement);
//        }
    }
}
