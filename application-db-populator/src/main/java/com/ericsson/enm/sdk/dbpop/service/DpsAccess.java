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

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.*;
import com.ericsson.oss.itpf.datalayer.dps.query.projection.Projection;

import javax.ejb.EJB;
import java.util.*;

import static com.ericsson.oss.itpf.datalayer.dps.BucketProperties.SUPPRESS_MEDIATION;

/**
 * Util class used to interact with the Live Bucket of the
 * DataPersistenceService.
 */
public class DpsAccess {

    @EJB(lookup = "java:/datalayer/DataPersistenceService")
    protected DataPersistenceService dps;

    public int deleteMo(final String fdn) {
        final ManagedObject mo = findMoInLiveBucket(fdn);
        return getLiveBucket().deletePo(mo);
    }

    public Collection<String> getAssociatedFdns(final String fdn, final String endpointName) {
        final Collection<String> associatedFdns = new ArrayList<>();
        final ManagedObject mo = findMoInLiveBucket(fdn);
        final Collection<PersistenceObject> associations = mo.getAssociations(endpointName);
        for (final PersistenceObject po : associations) {
            if (po instanceof ManagedObject) {
                associatedFdns.add(((ManagedObject) po).getFdn());
            }
        }
        return associatedFdns;
    }

    public Collection<String> getChildFdns(final String fdn) {
        final Collection<String> childFdns = new ArrayList<>();
        final ManagedObject mo = findMoInLiveBucket(fdn);
        final Collection<ManagedObject> children = mo.getChildren();
        for (final ManagedObject child : children) {
            childFdns.add(child.getFdn());
        }
        return childFdns;
    }

    public PersistenceObject findPo(final Long poId) {
        return getLiveBucket().findPoById(poId);
    }

    public int deletePo(final PersistenceObject po) {
        return getLiveBucket().deletePo(po);
    }

    public ManagedObject findMoByFdn(final String fdn) {
        return getLiveBucket().findMoByFdn(fdn);
    }

    public boolean hasChildren(final String fdn) {
        final ManagedObject mo = getLiveBucket().findMoByFdn(fdn);
        return mo.getChildren().size() > 0;
    }

    public ManagedObject createManagedObject(final String namespace, final String type, final String version,
            final String name, final Map<String, Object> attributes, final ManagedObject parent) {
        return getLiveBucket().getMibRootBuilder().namespace(namespace).type(type).name(name).version(version)
                .addAttributes(attributes).parent(parent).create();
    }

    public ManagedObject createManagedObjectWithTarget(final String namespace, final String type, final String version,
            final String name, final Map<String, Object> attributes, final ManagedObject parent,
            final PersistenceObject target) {
        return getLiveBucket().getMibRootBuilder().namespace(namespace).type(type).name(name).version(version)
                .addAttributes(attributes).target(target).parent(parent).create();
    }

    public PersistenceObject createPersistenceObject(final String namespace, final String type, final String version,
            final Map<String, Object> attributes) {
        return getLiveBucket().getPersistenceObjectBuilder().namespace(namespace).type(type).version(version)
                .addAttributes(attributes).create();
    }

    public Iterator<ManagedObject> executeTypeQuery(final String namespace, final String type) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        return queryExecutor.execute(getTypeQuery(namespace, type));
    }

    public Iterator<ManagedObject> executeTypeContainmentQuery(final String namespace, final String type,
            final String baseMoFdn) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        return queryExecutor.execute(getTypeContainmentQuery(namespace, type, baseMoFdn));
    }

    public long executeTypeContainmentQueryCount(final String namespace, final String type, final String baseMoFdn) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        return queryExecutor.executeCount(getTypeContainmentQuery(namespace, type, baseMoFdn));
    }

    public List<Object[]> executeNameProjectionQuery(final String namespace, final String type,
            final Projection initalProjection, final Projection[] furtherProjections) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        return queryExecutor.executeProjection(getTypeQuery(namespace, type), initalProjection, furtherProjections);
    }

    public List<PersistenceObject> executeTypeQueryWithAttributeRestriction(final String namespace, final String type,
            final String attribute, final Object attributeValue) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        final Query<TypeRestrictionBuilder> typeQuery = getTypeQuery(namespace, type);
        final Restriction restriction = typeQuery.getRestrictionBuilder().equalTo(attribute, attributeValue);
        typeQuery.setRestriction(restriction);
        return queryExecutor.<PersistenceObject>getResultList(typeQuery);
    }

    public List<PersistenceObject> executeTypeQueryWithAttributeRestriction(final String namespace, final String type,
            final Map<String, Object> attributes) {
        final QueryExecutor queryExecutor = getQueryExecutor();
        final Query<TypeRestrictionBuilder> typeQuery = getTypeQuery(namespace, type);
        final List<Restriction> restrictions = new ArrayList<>();
        for (final String attribute : attributes.keySet()) {
            restrictions.add(typeQuery.getRestrictionBuilder().equalTo(attribute, attributes.get(attribute)));
        }
        final Restriction restriction = typeQuery.getRestrictionBuilder()
                .allOf(restrictions.toArray(new Restriction[restrictions.size()]));
        typeQuery.setRestriction(restriction);
        return queryExecutor.<PersistenceObject>getResultList(typeQuery);
    }

    private ManagedObject findMoInLiveBucket(final String fdn) {
        final DataBucket bucket = getLiveBucket();
        final ManagedObject mo = bucket.findMoByFdn(fdn);
        return mo;
    }

    private Query<TypeRestrictionBuilder> getTypeQuery(final String namespace, final String type) {
        final QueryBuilder queryBuilder = dps.getQueryBuilder();
        return queryBuilder.createTypeQuery(namespace, type);
    }

    private Query<TypeContainmentRestrictionBuilder> getTypeContainmentQuery(final String namespace, final String type,
            final String baseMoFdn) {
        final QueryBuilder queryBuilder = dps.getQueryBuilder();
        return queryBuilder.createTypeQuery(namespace, type, baseMoFdn);
    }

    private QueryExecutor getQueryExecutor() {
        return getLiveBucket().getQueryExecutor();
    }

    private DataBucket getLiveBucket() {
        return dps.getDataBucket("live", SUPPRESS_MEDIATION);
    }
}