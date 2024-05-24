package com.sean.web.service;

import com.sean.web.vo.MemberExternalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiService {

    private final ResTemplateService restTemplateService;

    public MemberExternalVO getExternalDataStore() {
        MemberExternalVO forObject = null;
        try {
            forObject = (MemberExternalVO) restTemplateService.getJsonObject("https://apiservice.mol.gov.tw/OdService/rest/datastore/A17000000J-020001-kvg", MemberExternalVO.class);
        } catch (Exception e) {
            log.error("getExternalData error", e);
        }
        return forObject;
    }
}
