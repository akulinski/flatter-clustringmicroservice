package com.flatter.server.clusteringmicroservice.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionnaireableUser extends Questionnaireable {

    private String login;

    private String firstName;

    private String lastName;

    private String email;
}
