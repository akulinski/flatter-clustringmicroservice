package com.flatter.server.clusteringmicroservice.core.repositories;

import com.flatter.server.clusteringmicroservice.core.domain.documents.ClusteringDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClusteringDocumentRepository extends ElasticsearchRepository<ClusteringDocument, String> {
    List<ClusteringDocument> findByQuestionnaireable_Name(String name);

    Integer countAllByQuestionnaireable_Name(String name);

}
