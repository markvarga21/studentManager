package com.markvarga21.usermanager.service.azure;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.markvarga21.usermanager.service.azure.impl.FormRecognizerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FormRecognizerServiceTest {
    @InjectMocks
    private FormRecognizerServiceImpl formRecognizerService;
    @Mock
    private DocumentAnalysisClient documentAnalysisClient;

    @Test
    void testValidateUserShouldDoNothingWhenIdContentIdValidTest() {
        // Given


        // When

        // Then
    }

}
