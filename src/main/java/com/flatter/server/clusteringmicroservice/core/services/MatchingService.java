package com.flatter.server.clusteringmicroservice.core.services;

import com.google.common.cache.Cache;
import domain.Questionnaireable;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MatchingService {

    private final Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache;

    private final Cache<String, Questionnaireable> loginQuestionnaireableCache;


    @Autowired
    public MatchingService(Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache, Cache<String, Questionnaireable> loginQuestionnaireableCache) {
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
        this.loginQuestionnaireableCache = loginQuestionnaireableCache;
    }


    public Stream<Map.Entry<Questionnaireable, Double>> getSortedMatches(String login) {

        Questionnaireable questionnaireable = loginQuestionnaireableCache.getIfPresent(login);

        CentroidCluster<Questionnaireable> questionnaireableCentroidCluster = questionnaireableCentroidClusterCache.getIfPresent(questionnaireable);

        HashMap<Questionnaireable, Double> differnceBettwenPointAndCurrentUser = new HashMap<>();

        final List<Questionnaireable> offers = questionnaireableCentroidCluster.getPoints().stream().filter(questionnaireable1 -> questionnaireable1.getName().equals("offer")).collect(Collectors.toList());

        offers.forEach(offer -> {
            try {
                differnceBettwenPointAndCurrentUser.put(offer, questionnaireable.getSumOfPoints() - offer.getSumOfPoints());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return differnceBettwenPointAndCurrentUser.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());
    }

}
