package com.flatter.server.clusteringmicroservice.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import static com.flatter.server.clusteringmicroservice.utils.ClusteringUtils.weightMap;

@Data
@NoArgsConstructor
@Log4j2
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestionnaireableUser.class, name = "user"),

        @JsonSubTypes.Type(value = QuestionnaireableOffer.class, name = "offer")}
)
public abstract class Questionnaireable implements Clusterable, CSVWritable, Comparable<Questionnaireable>, Serializable {

    @JsonProperty
    private String name;

    @JsonProperty
    private boolean pets;

    @JsonProperty
    private boolean smokingInside;

    @JsonProperty
    private boolean isFurnished;

    @JsonProperty
    private int roomAmountMin;

    @JsonProperty
    private int roomAmountMax;

    @JsonProperty
    private double sizeMin;

    @JsonProperty
    private double sizeMax;

    @JsonProperty
    private int constructionYearMin;

    @JsonProperty
    private int constructionYearMax;

    @JsonProperty
    private String type;

    @JsonProperty
    private double totalCostMin;

    @JsonProperty
    private double totalCostMax;

    @CreatedDate
    private Date dateOfCreation;

    @LastModifiedDate
    private Date dateOfModification;


    @Override
    public double[] getPoint() {
        try {
            return this.getArrayOfPoints();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new double[0];
    }

    @Override
    public int compareTo(Questionnaireable o) {

        try {
            return (int) (this.getSumOfPoints()-o.getSumOfPoints());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getSumOfPoints() throws IllegalAccessException {
        double sum = 0;
        for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {

            if (checkIfFieldIsBooleanType(field)) {
                boolean booleanValueOfField = field.getBoolean(this);
                if (booleanValueOfField) {
                    sum += 1 * weightMap.get(field.getName());
                }
            } else if (checkIfFiledIsInteger(field)) {
                sum += field.getInt(this) * weightMap.get(field.getName());
            } else if (checkIfDouble(field)) {
                sum += field.getDouble(this) * weightMap.get(field.getName());
            }
        }
        log.debug("getSumOfPoints called on object: " + this.toString() + " calculated sum of fields is " + sum);
        return sum;
    }

    public double[] getArrayOfPoints() throws IllegalAccessException {
        double[] arrayOfPoints = new double[this.getClass().getSuperclass().getDeclaredFields().length];
        int index = 0;
        double sum = 0;
        for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {

            if (checkIfFieldIsBooleanType(field)) {
                boolean booleanValueOfField = field.getBoolean(this);
                if (booleanValueOfField) {
                    sum += 1 * weightMap.get(field.getName());
                    arrayOfPoints[index] = 1 * weightMap.get(field.getName());
                    ;
                }
            } else if (checkIfFiledIsInteger(field)) {
                sum += field.getInt(this) * weightMap.get(field.getName());
                arrayOfPoints[index] = field.getInt(this) * weightMap.get(field.getName());
            } else if (checkIfDouble(field)) {
                sum += field.getDouble(this) * weightMap.get(field.getName());
                arrayOfPoints[index] = field.getDouble(this) * weightMap.get(field.getName());
            }
            index++;
        }
        log.debug("getArrayOfPoints called on object: " + this.toString() + " calculated sum of fields is " + sum);
        return arrayOfPoints;
    }


    private static boolean checkIfDouble(Field field) {
        return field.getType() == double.class;
    }

    private static boolean checkIfFiledIsInteger(Field field) {
        return field.getType() == int.class;
    }

    private static boolean checkIfFieldIsBooleanType(Field field) {
        return field.getType() == boolean.class;
    }

    @Override
    public ArrayList<String> getData() throws IllegalAccessException {
        Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
        ArrayList<String> data = new ArrayList<>();

        for (Field field : fields) {

            if (checkIfFieldIsBooleanType(field)) {
                boolean booleanValueOfField = field.getBoolean(this);
                data.add(String.valueOf(booleanValueOfField));
            } else if (checkIfFiledIsInteger(field)) {
                int fieldInt = field.getInt(this);
                data.add(String.valueOf(fieldInt));

            } else if (checkIfDouble(field)) {
                double fieldDouble = field.getDouble(this);
                data.add(String.valueOf(fieldDouble));
            }

        }

        return data;
    }
}
