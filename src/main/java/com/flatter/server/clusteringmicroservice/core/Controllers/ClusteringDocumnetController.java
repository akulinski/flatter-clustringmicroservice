package com.flatter.server.clusteringmicroservice.core.Controllers;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.flatter.server.clusteringmicroservice.core.services.MatchingService;
import com.google.common.cache.Cache;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
@RequestMapping("/api")
public class ClusteringDocumnetController {

    private final Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache;

    private final Cache<String, Questionnaireable> loginQuestionnaireableCache;

    private final Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache;

    private final MatchingService matchingService;

    @Autowired
    public ClusteringDocumnetController(Cache<String, Questionnaireable> loginQuestionnaireableCache, Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache, Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache, MatchingService matchingService) {
        this.loginQuestionnaireableCache = loginQuestionnaireableCache;
        this.stringCentroidClusterCache = stringCentroidClusterCache;
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
        this.matchingService = matchingService;
    }

    @GetMapping("/get-matches/{login}")
    public ResponseEntity getMatchesOfUser(@PathVariable String login) {

        LinkedList<Questionnaireable> questionnaireables = new LinkedList<>();

        matchingService.getSortedMatches(login).forEach(questionnaireableDoubleEntry -> {
            questionnaireables.add(questionnaireableDoubleEntry.getKey());
        });

        return new ResponseEntity<>(questionnaireables, HttpStatus.OK);
    }
}
