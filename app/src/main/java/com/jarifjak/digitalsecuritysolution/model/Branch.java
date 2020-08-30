package com.jarifjak.digitalsecuritysolution.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "branch_table")
@Keep
public class Branch {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    private int id;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("bankCode")
    @Expose
    private String bankCode;

    @SerializedName("firstManagerName")
    @Expose
    private String firstManagerName;

    @SerializedName("firstManagerNumber")
    @Expose
    private String firstManagerNumber;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("secondManagerName")
    @Expose
    private String secondManagerName;

    @SerializedName("secondManagerNumber")
    @Expose
    private String secondManagerNumber;

    public Branch() {
    }

    public Branch(int id, String address, String bankCode, String name) {

        this.id = id;
        this.address = address;
        this.bankCode = bankCode;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstManagerName() {
        return firstManagerName;
    }

    public void setFirstManagerName(String firstManagerName) {
        this.firstManagerName = firstManagerName;
    }

    public String getFirstManagerNumber() {
        return firstManagerNumber;
    }

    public void setFirstManagerNumber(String firstManagerNumber) {
        this.firstManagerNumber = firstManagerNumber;
    }

    public String getSecondManagerName() {
        return secondManagerName;
    }

    public void setSecondManagerName(String secondManagerName) {
        this.secondManagerName = secondManagerName;
    }

    public String getSecondManagerNumber() {
        return secondManagerNumber;
    }

    public void setSecondManagerNumber(String secondManagerNumber) {
        this.secondManagerNumber = secondManagerNumber;
    }


}
