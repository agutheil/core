package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.enumeration.Currency;
import com.mightymerce.core.domain.enumeration.Tax;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.dto.LookupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smhumayun on 10/3/2015.
 */
@RestController
@RequestMapping("/api")
public class LookupResource {

    private final Logger log = LoggerFactory.getLogger(LookupResource.class);

    /**
     * GET  /lookup/:lookupName -> get the "lookupName" lookup.
     */
    @RequestMapping(value = "/lookup/{lookupName}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LookupDTO>> get(@PathVariable String lookupName) {
        log.debug("REST request by '{}' to get Lookup : {}", SecurityUtils.getCurrentLogin(), lookupName);
        List<LookupDTO> lookupDTOList = null;
        if(lookupName.equals("tax"))
            lookupDTOList = getTax();
        else if(lookupName.equals("currency"))
            lookupDTOList = getCurrency();
        return lookupDTOList != null ? new ResponseEntity<>(lookupDTOList, HttpStatus.OK)
            : ResponseEntity.badRequest().header("Failure", "Lookup not defined").body(null);
    }

    private List<LookupDTO> getTax () {
        List<LookupDTO> lookupDTOList = new ArrayList<>();
        for (Tax anEnum : Tax.values()) {
            lookupDTOList.add(new LookupDTO(anEnum.getKey(), anEnum.getValue()));
        }
        return lookupDTOList;
    }

    private List<LookupDTO> getCurrency () {
        List<LookupDTO> lookupDTOList = new ArrayList<>();
        for (Currency anEnum : Currency.values()) {
            lookupDTOList.add(new LookupDTO(anEnum.getKey(), anEnum.getValue()));
        }
        return lookupDTOList;
    }

}
