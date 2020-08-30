package com.jarifjak.digitalsecuritysolution.model;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "employee_table")
@Keep
public class Employee {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;

    @SerializedName("bankCode")
    @Expose
    private String bankCode;

    @SerializedName("branchName")
    @Expose
    private String branchName;

    @SerializedName("number")
    @Expose
    private String number;

    public Employee() {
    }

    public Employee(int id, String name, String accountNumber, String bankCode, String branchName, String number) {

        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.branchName = branchName;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
