package com.welcomeletterservice.service.impl;

import com.welcomeletterservice.service.CorrespondenceService;
import com.welcomeletterservice.service.WelcomeLetterService;
import com.welcomeletterservice.service.response.CorrespondenceResponse;
import org.springframework.stereotype.Service;

@Service
public class WelcomeLetterServiceImpl implements WelcomeLetterService {

    private final CorrespondenceService correspondenceService;

    public WelcomeLetterServiceImpl(CorrespondenceService correspondenceService) {
        this.correspondenceService = correspondenceService;
    }

    @Override
    public CorrespondenceResponse letterGeneration() {
        CorrespondenceResponse response = correspondenceService.letterGeneration();
        return response;
    }

}
