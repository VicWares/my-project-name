package com.wintrisstech;
/********************************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 211010
 * Reads Bet365 Odds from Covers...https://www.covers.com/sport/football/nfl/odds
 ********************************************************************************/

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
public class OddsCollector
{
    private static HashMap<String, String> bet365HomeTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365AwayTeamOdds = new HashMap<>();
    public static void collectThisWeekOdds(String dataEventId, Elements oddsElements)
    {
        int i = 0;
        System.out.println("oddElements.size => " + oddsElements.size());
        for (Element e : oddsElements.select(".liveOddsCell, data-book, data-game"))
        {
            //if (i % 5 == 0)
            {
                String span = e.select("span:nth-child(1)").text();
                String nums[] = span.split(" ");
                //System.out.print("game " + i + " " + nums[0]  + " " + nums[5] + "\n");
            }
            i++;
        }
        Element nChild1 = oddsElements.select("div:nth-child(1) span").get(0);//bet365 home moneyline TODO:Get rid of hard select
        Element nChild5 = oddsElements.select("div:nth-child(1) span").get(5);//bet365 away moneyline
        bet365HomeTeamOdds.put(dataEventId, nChild1.text());
        bet365AwayTeamOdds.put(dataEventId, nChild5.text());
        System.out.println(bet365AwayTeamOdds);
        System.out.println(nChild1.text());
        System.out.println("======================================================");
        System.out.println(nChild5.text());
    }
}
