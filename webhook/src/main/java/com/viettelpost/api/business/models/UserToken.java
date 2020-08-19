package com.viettelpost.api.business.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettelpost.api.base.Constants;

import java.io.Serializable;
import java.util.Date;

public class UserToken implements Serializable {
    @JsonIgnore
    Long userId;
    String token;
    @JsonIgnore
    Long partner;
    @JsonIgnore
    String phone;
    @JsonIgnore
    long expired;
    @JsonIgnore
    String encrypted;
    @JsonIgnore
    int source;

    public UserToken() {
    }

    public UserToken(String token) {
        this.token = token;
    }

    public UserToken(Long userId, String token, Long partner, String phone, int source) {
        this.userId = userId;
        this.token = token;
        this.partner = partner;
        this.phone = phone;
        this.expired = new Date().getTime() + Constants.expired;
        this.source = source;
    }

    public UserToken(Long userId, String token, Long partner, long expired) {
        this.userId = userId;
        this.token = token;
        this.partner = partner;
        this.expired = expired;
    }

    public UserToken(Long userId, String token, Long partner, String phone, long expired) {
        this.userId = userId;
        this.token = token;
        this.partner = partner;
        this.phone = phone;
        this.expired = expired;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public Long getPartner() {
        return partner;
    }

    public void setPartner(Long partner) {
        this.partner = partner;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
