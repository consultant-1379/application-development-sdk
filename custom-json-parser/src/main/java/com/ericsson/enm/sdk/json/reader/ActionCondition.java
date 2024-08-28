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
import org.codehaus.jackson.map.annotate.JsonSerialize;
*/
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ActionCondition {
    private String dataType;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<ActionProperties> properties;

    /**
     * @return the dataType
     */

    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType
     *            the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the properties
     */
    public List<ActionProperties> getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            the properties to set
     */
    public void setProperties(List<ActionProperties> properties) {
        this.properties = properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ActionCondition [dataType=" + dataType + ", properties=" + properties + "]";
    }

}
