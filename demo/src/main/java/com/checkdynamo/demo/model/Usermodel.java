package com.checkdynamo.demo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Usermodel")
public class Usermodel {


        @DynamoDBHashKey
        @DynamoDBAutoGeneratedKey
        private String userId;
    @DynamoDBAttribute
        private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
 @DynamoDBAttribute
    private Role role;
    @DynamoDBIndexHashKey(globalSecondaryIndexName ="userEmail-index")
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    @DynamoDBAttribute
private String phoneno;
    @DynamoDBAttribute
    private Confirmationtoken confirm;

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @DynamoDBAttribute
        private String userPassword;
}
