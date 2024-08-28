/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.enm.sdk.json.reader;

import java.util.List;
/*
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
*/
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomConfigJson {

    private List<CustomApplication> customApplications;

    /**
     * @return the customApplications
     */
    public List<CustomApplication> getCustomApplications() {
        return customApplications;
    }

    /**
     * @param customApplications the customApplications to set
     */
    public void setCustomApplications(List<CustomApplication> customApplications) {
        this.customApplications = customApplications;
    }
}
