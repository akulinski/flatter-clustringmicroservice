package com.flatter.server.clusteringmicroservice.utils;

import com.flatter.server.clusteringmicroservice.core.domain.Questionnaireable;
import com.flatter.server.clusteringmicroservice.core.domain.QuestionnaireableUser;
import org.junit.Test;

public class ClusteringUtilsTest {

    @Test
    public void calcSumOfWeights() throws IllegalAccessException {
        Questionnaireable questionnaireable = new QuestionnaireableUser();
        questionnaireable.setSmokingInside(true);
        questionnaireable.setSizeMin(30.00);
        questionnaireable.setSizeMax(100.00);
        questionnaireable.setRoomAmountMin(2);
        questionnaireable.setRoomAmountMax(5);
        questionnaireable.setPets(true);
        questionnaireable.setFurnished(false);
        questionnaireable.setTotalCostMin(100.00);
        questionnaireable.setTotalCostMax(2000.00);
        questionnaireable.setConstructionYearMin(2000);
        questionnaireable.setConstructionYearMax(2019);

        System.out.println(questionnaireable.getArrayOfPoints());
    }
}
