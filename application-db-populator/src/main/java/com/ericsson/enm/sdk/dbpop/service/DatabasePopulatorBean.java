/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.sdk.dbpop.service;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the army database.
 */
@Stateless
public class DatabasePopulatorBean {

    private Logger logger  = LoggerFactory.getLogger(DatabasePopulatorBean.class);

    @Inject
    private DpsAccess dpsAccess;

    /**
     * Creates a new managed object with the target.
     *
     * @param ns
     *            namespace for the managed object.
     * @param type
     *            type of the managed object (e.g. NetworkElement).
     * @param name
     *            name the name of the ne (e.g. RNC01).
     * @param neType
     *            neType the neType (e.g. RadioNode, RBS, MSRBS_V1, etc...).
     * @return the created/existing managed object.
     */
    public ManagedObject createMoIfNotExists(final String ns, final String type, final String name, final String neType) {
        logger.info(" => Creating mo with ns {}, type {}, name {} and neType {}", ns, type, name, neType);
        ManagedObject mo = dpsAccess.findMoByFdn(type + "=" + name);
        if (mo == null) {
            final Map<String, Object> attributes = new HashMap<>();
            attributes.put("category", "NODE");
            attributes.put("type", type);
            attributes.put("name", name);
            attributes.put("modelIdentity", "someIdentity");
            final PersistenceObject target = dpsAccess.createPersistenceObject("DPS", "Target", "1.0.0", attributes);
            final Map<String, Object> moAttributes = new HashMap<>();
            moAttributes.put("neType", neType);
            mo = dpsAccess.createManagedObjectWithTarget(ns, type, "2.0.0", name, moAttributes, null, target);

        }
        return mo;
    }
}