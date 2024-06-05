package com.example.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalenderId implements Serializable {

    private Long groupId;
    private LocalDate cDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalenderId that = (CalenderId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(cDate, that.cDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, cDate);
    }
}