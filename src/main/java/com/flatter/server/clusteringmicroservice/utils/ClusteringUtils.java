package com.flatter.server.clusteringmicroservice.utils;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ClusteringUtils {
    public final static Map<String, Double> weightMap = new HashMap<>();
    public final static String[] HEEADERS = new String[]{"cluster#","pets","smokingInside","isFurnished","roomAmountMin"
    ,"roomAmountMax","sizeMin","sizeMax","constructionYearMin","constructionYearMax","totalCostMin","totalCostMax"};
    //1000.00 is max weight
    static {
        weightMap.put("pets", 1000.00);
        weightMap.put("smokingInside", 700.00);
        weightMap.put("isFurnished", 500.5);
        weightMap.put("roomAmountMin", 50.00); //per one room
        weightMap.put("roomAmountMax", 20.00);
        weightMap.put("sizeMin", 80.00); //per m^2
        weightMap.put("sizeMax", 20.00);
        weightMap.put("constructionYearMin", 1.20);
        weightMap.put("constructionYearMax", 1.00);
        weightMap.put("type", 1000.00);
        weightMap.put("totalCostMin", 10.00); //per one zl
        weightMap.put("totalCostMax", 100.00);
    }

}
