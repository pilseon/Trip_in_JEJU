package com.example.Trip_In_Jeju.member.entity;


import lombok.Getter;

@Getter
public enum MemberRole {

    // admin user 역할 설정

    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private String value;
    MemberRole(String value) {
        this.value = value;
    }
}