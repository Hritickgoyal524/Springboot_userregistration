package com.checkdynamo.demo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Role {

    @DynamoDBAttribute
    private String roletype;

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getRoledes() {
        return roledes;
    }

    public void setRoledes(String roledes) {
        this.roledes = roledes;
    }

    @DynamoDBAttribute
    private  String roledes;
}