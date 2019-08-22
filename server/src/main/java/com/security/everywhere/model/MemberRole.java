package com.security.everywhere.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String roleName;


    public Long getRno() {
        return rno;
    }

    public void setRno(Long rno) {
        this.rno = rno;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberRole that = (MemberRole) o;
        return Objects.equals(rno, that.rno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rno);
    }

    @Override
    public String toString() {
        return "MemberRole{" +
                "rno=" + rno +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
