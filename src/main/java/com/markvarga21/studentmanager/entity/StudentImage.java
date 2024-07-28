package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.util.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the images of students.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class StudentImage {
    /**
     * The passport number of the student.
     */
    @Id
    private Long studentId;

    /**
     * The passport image of the student.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] passportImage;

    /**
     * The selfie image of the student.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] selfieImage;
}
