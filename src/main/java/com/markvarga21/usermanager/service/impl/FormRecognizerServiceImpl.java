package com.markvarga21.usermanager.service.impl;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.exception.InvalidIdDocumentException;
import com.markvarga21.usermanager.service.FormRecognizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FormRecognizerServiceImpl implements FormRecognizerService {
    private final DocumentAnalysisClient documentAnalysisClient;

    @Override
    public boolean validateUser(AppUserDto appUserDto, MultipartFile idDocument) {
        File layoutDocument = new File(Objects.requireNonNull(idDocument.getOriginalFilename()));
        try {
            idDocument.transferTo(layoutDocument);
        } catch (IOException exception) {
            throw new InvalidIdDocumentException("ID document not valid/not found!");
        }
        Path filePath = layoutDocument.toPath();
        BinaryData layoutDocumentData = BinaryData.fromFile(filePath);
        String modelId = "prebuilt-idDocument";


        SyncPoller < OperationResult, AnalyzeResult > analyzeDocumentPoller =
                this.documentAnalysisClient.beginAnalyzeDocument(modelId, layoutDocumentData);

        AnalyzeResult analyzeResult = analyzeDocumentPoller.getFinalResult();
        var documentResult = analyzeResult.getDocuments().get(0);
        System.out.println(documentResult.getFields().get("FirstName").getContent());

        return true;
    }
}
