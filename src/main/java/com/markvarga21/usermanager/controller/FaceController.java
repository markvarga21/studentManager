package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.dto.FacialValidationData;
import com.markvarga21.usermanager.dto.FaceApiResponse;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A controller which is used to make Face related operations.
 */
@RestController
@RequestMapping("/api/v1/faces")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class FaceController {
    /**
     * Face service.
     */
    private final FaceApiService faceApiService;

    /**
     * Compares the faces found on the passport and the
     * portrait, and then sends it back to the client.
     *
     * @param passport The user's passport.
     * @param selfiePhoto The portrait of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @return The validity and the percentage of the matching.
     */
    @PostMapping("/validate")
    public ResponseEntity<FaceApiResponse> getPortraitValidationData(
            @RequestParam("passport") final MultipartFile passport,
            @RequestParam("selfiePhoto") final MultipartFile selfiePhoto,
            @RequestParam("firstName") final String firstName,
            @RequestParam("lastName") final String lastName
    ) {
        FaceApiResponse faceApiResponse =
                this.faceApiService.getValidityOfFaces(
                        passport, selfiePhoto, firstName, lastName
                );

        return new ResponseEntity<>(faceApiResponse, HttpStatus.OK);
    }

    /**
     * Returns all the facial validation data.
     *
     * @return All the facial validation data.
     */
    @GetMapping
    public List<FacialValidationData> getAllFacialValidationData() {
        return this.faceApiService.fetchAllValidationData();
    }

    /**
     * Deletes the facial validation data by ID.
     *
     * @param id The ID of the facial validation data.
     */
    @DeleteMapping("/{id}")
    public void deleteFacialValidationData(
            @PathVariable("id") final Long id
    ) {
        this.faceApiService.deleteFacialValidationData(id);
    }
}
