package com.example.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberId implements Serializable {

    private Long groupId;
    private String userID;

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(groupId, userID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemberId that = (MemberId) obj;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(userID, that.userID);
    }

}
