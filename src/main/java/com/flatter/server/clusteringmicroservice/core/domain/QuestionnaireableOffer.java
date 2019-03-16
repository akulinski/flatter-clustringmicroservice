package com.flatter.server.clusteringmicroservice.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionnaireableOffer extends Questionnaireable {
    private String description;

}
