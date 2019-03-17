package com.flatter.server.clusteringmicroservice.core.services;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.google.common.cache.Cache;
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

    private final Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache;

    @Autowired
    public MatchingService(Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache, Cache<String, Questionnaireable> loginQuestionnaireableCache, Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache) {
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
        this.loginQuestionnaireableCache = loginQuestionnaireableCache;
        this.stringCentroidClusterCache = stringCentroidClusterCache;
    }


    public Stream<Map.Entry<Questionnaireable,Double>> getSortedMatches(String login){
        Questionnaireable questionnaireable = loginQuestionnaireableCache.getIfPresent(login);

        CentroidCluster<Questionnaireable> questionnaireableCentroidCluster = questionnaireableCentroidClusterCache.getIfPresent(questionnaireable);

        HashMap<Questionnaireable,Double> differnceBettwenPointAndCurrentUser = new HashMap<>();

        final List<Questionnaireable> flats = questionnaireableCentroidCluster.getPoints().stream().filter(questionnaireable1 -> questionnaireable1.getName().equals("offer")).collect(Collectors.toList());

        flats.forEach(flat->{
            try {
                differnceBettwenPointAndCurrentUser.put(flat,questionnaireable.getSumOfPoints()-flat.getSumOfPoints());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return differnceBettwenPointAndCurrentUser.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue());
    }

}
