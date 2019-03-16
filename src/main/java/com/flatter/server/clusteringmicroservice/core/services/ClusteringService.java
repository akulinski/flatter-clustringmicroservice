package com.flatter.server.clusteringmicroservice.core.services;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.flatter.server.clusteringmicroservice.core.domain.QuestionnaireableUser;
import com.flatter.server.clusteringmicroservice.core.domain.documents.ClusteringDocument;
import com.flatter.server.clusteringmicroservice.core.repositories.ClusteringDocumentRepository;
import com.flatter.server.clusteringmicroservice.utils.ClusteringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
public class ClusteringService {

    private final ClusteringDocumentRepository clusteringDocumentRepository;

    @Autowired
    public ClusteringService(ClusteringDocumentRepository clusteringDocumentRepository) {
        this.clusteringDocumentRepository = clusteringDocumentRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void clusterData() throws IOException, IllegalAccessException {
        Iterable<ClusteringDocument> documentRepositoryAll = clusteringDocumentRepository.findAll();
        LinkedList<QuestionnaireableUser> questionnaireableLinkedList = new LinkedList<>();

        documentRepositoryAll.forEach(clusteringDocument -> {
            try {
                questionnaireableLinkedList.add((QuestionnaireableUser) clusteringDocument.getQuestionnaireable());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        KMeansPlusPlusClusterer<QuestionnaireableUser> clusterer = new KMeansPlusPlusClusterer<>(100, 100000);
        List<CentroidCluster<QuestionnaireableUser>> clusterResults = clusterer.cluster(questionnaireableLinkedList);

        FileWriter out = new FileWriter("data.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(ClusteringUtils.HEEADERS))) {
            for (int i = 0; i < clusterResults.size(); i++) {
                log.debug("Cluster " + i);
                for (Questionnaireable questionnaireable : clusterResults.get(i).getPoints()) {
                    log.debug("Writing data " + questionnaireable.getData());
                    printer.printRecord(i, questionnaireable.getData());
                }
            }
        }

        out.close();
    }
}
