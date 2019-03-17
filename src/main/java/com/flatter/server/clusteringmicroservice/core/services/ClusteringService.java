package com.flatter.server.clusteringmicroservice.core.services;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.flatter.server.clusteringmicroservice.core.domain.QuestionnaireableUser;
import com.flatter.server.clusteringmicroservice.core.domain.documents.ClusteringDocument;
import com.flatter.server.clusteringmicroservice.core.repositories.ClusteringDocumentRepository;
import com.flatter.server.clusteringmicroservice.utils.ClusteringUtils;
import com.google.common.cache.Cache;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.toIntExact;

@Service
@Log4j2
public class ClusteringService {

    private final ClusteringDocumentRepository clusteringDocumentRepository;

    private final Cache<String, Questionnaireable> loginQuestionnaireableCache;

    private final Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache;

    private final Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache;

    @Autowired
    public ClusteringService(ClusteringDocumentRepository clusteringDocumentRepository, Cache<String, Questionnaireable> stringQuestionnaireableCache, Cache<String, CentroidCluster<Questionnaireable>> stringCentroidClusterCache, Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache) {
        this.clusteringDocumentRepository = clusteringDocumentRepository;
        this.loginQuestionnaireableCache = stringQuestionnaireableCache;
        this.stringCentroidClusterCache = stringCentroidClusterCache;
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void clusterData() throws IOException, IllegalAccessException, InterruptedException {
        while (true) {
            Iterable<ClusteringDocument> documentRepositoryAll = clusteringDocumentRepository.findAll();
            LinkedList<Questionnaireable> questionnaireableLinkedList = new LinkedList<>();

            documentRepositoryAll.forEach(clusteringDocument -> {
                try {
                    questionnaireableLinkedList.add(clusteringDocument.getQuestionnaireable());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            int count = toIntExact (clusteringDocumentRepository.count()*100);
            KMeansPlusPlusClusterer<Questionnaireable> clusterer = new KMeansPlusPlusClusterer<>(clusteringDocumentRepository.countAllByQuestionnaireable_Name("offer"), count);
            List<CentroidCluster<Questionnaireable>> clusterResults = clusterer.cluster(questionnaireableLinkedList);

            loginQuestionnaireableCache.invalidateAll();
            stringCentroidClusterCache.invalidateAll();
            questionnaireableCentroidClusterCache.invalidateAll();

            FileWriter out = new FileWriter("data/data" + new Date().toString() + ".csv");

            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(ClusteringUtils.HEEADERS))) {
                for (int i = 0; i < clusterResults.size(); i++) {
                    log.debug("Cluster " + i);
                    stringCentroidClusterCache.put(clusterResults.get(i).toString(), clusterResults.get(i));
                    for (Questionnaireable questionnaireable : clusterResults.get(i).getPoints()) {
                        log.debug("Writing data " + questionnaireable.getData());
                        questionnaireableCentroidClusterCache.put(questionnaireable, clusterResults.get(i));

                        if (questionnaireable.getName().equals("user")) {
                            QuestionnaireableUser questionnaireableUser = (QuestionnaireableUser) questionnaireable;
                            loginQuestionnaireableCache.put(questionnaireableUser.getLogin(), questionnaireable);
                        }
                        printer.printRecord(i, questionnaireable.getData());
                    }
                }
            }

            out.close();

            Thread.sleep(10000);
        }
    }
}
