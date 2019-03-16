package com.flatter.server.clusteringmicroservice.core.repositories;

import com.flatter.server.clusteringmicroservice.core.domain.documents.ClusteringDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusteringDocumentRepository extends ElasticsearchRepository<ClusteringDocument, String> {

}
