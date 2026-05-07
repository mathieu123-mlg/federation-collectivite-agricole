package edu.hei.school.agricultural.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectivityInformation {
    private String name;
    private Integer number;
}