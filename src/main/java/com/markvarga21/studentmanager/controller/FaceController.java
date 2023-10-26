package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.FaceApiResponse;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * A controller which is used to make Face related operations.
 */
@RestController
@RequestMapping("/api/v1/faces")
@RequiredArgsConstructor
@CrossOrigin
public class FaceController {
    /**
     * Face service.
     */
    private final FaceApiService faceApiService;

    /**
     * Compares the faces found on the passport and the
     * selfie, and then sends it back to the client.
     *
     * @param passport The student's passport.
     * @param selfiePhoto The selfie of the student.
     * @return The validity and the percentage of the matching.
     */
    @PostMapping("/validate")
    public ResponseEntity<FaceApiResponse> getSelfieValidationData(
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfiePhoto") final MultipartFile selfiePhoto
    ) {
        FaceApiResponse faceApiResponse =
                this.faceApiService.getValidityOfFaces(
                        passport, selfiePhoto
                );

        return new ResponseEntity<>(faceApiResponse, HttpStatus.OK);
    }
}
