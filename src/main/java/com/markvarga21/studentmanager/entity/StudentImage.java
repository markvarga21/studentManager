package com.markvarga21.studentmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentImage {
    /**
     * The passport number of the student.
     */
    @Id
    private String passportNumber;

    /**
     * The image of the student.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] passportImage;

    /**
     * The selfie of the student.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] selfieImage;
}
