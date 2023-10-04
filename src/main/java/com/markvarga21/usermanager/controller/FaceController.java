package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.entity.FacialValidationData;
import com.markvarga21.usermanager.service.faceapi.FaceApiResponse;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * portrait, and then sends it back to the client.
     *
     * @param passport the user's passport.
     * @param selfiePhoto the portrait of the user.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return the validity and the percentage of the matching.
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
     * @return all the facial validation data.
     */
    @GetMapping
    public List<FacialValidationData> getAllFacialValidationData() {
        return this.faceApiService.fetchAllValidationData();
    }

    /**
     * Deletes the facial validation data by ID.
     *
     * @param id the ID of the facial validation data.
     */
    @DeleteMapping("/{id}")
    public void deleteFacialValidationData(
            @PathVariable("id") final Long id
    ) {
        this.faceApiService.deleteFacialValidationData(id);
    }
}
