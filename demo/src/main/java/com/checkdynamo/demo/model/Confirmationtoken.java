package com.checkdynamo.demo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Confirmationtoken {
    @DynamoDBAttribute
    private String confirmtoken;

    public String getConfirmtoken() {
        return confirmtoken;
    }

    public void setConfirmtoken(String confirmtoken) {
        this.confirmtoken = confirmtoken;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @DynamoDBAttribute
    private Date createdate;
}
