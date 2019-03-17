package com.flatter.server.clusteringmicroservice.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class QuestionnaireableOffer extends Questionnaireable implements Serializable {
    private String description;

}
