package com.flatter.server.clusteringmicroservice.core.domain;

import java.util.ArrayList;

public interface CSVWritable {
    public ArrayList<String> getData() throws IllegalAccessException;
}
