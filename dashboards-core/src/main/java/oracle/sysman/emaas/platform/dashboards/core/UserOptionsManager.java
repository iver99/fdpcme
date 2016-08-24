/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author jishshi
 */
public class UserOptionsManager {
    private static final Logger LOGGER = LogManager.getLogger(UserOptionsManager.class);

    private static final UserOptionsManager instance = new UserOptionsManager();
    public static final Long DEFAULT_REFRESH_INTERVAL = 300000L;

    public static UserOptionsManager getInstance() {
        return instance;
    }

    private UserOptionsManager() {
        super();
    }

    public UserOptions getOptionsById(Long dashboardId, Long tenantId) throws DashboardException {
        EntityManager em = null;
        try {
            String currentUser = UserContext.getCurrentUser();
            DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
            em = dsf.getEntityManager();

            if (dashboardId == null || dashboardId <= 0) {
                LOGGER.debug("Dashboard not found for id {} is invalid", dashboardId);
                throw new DashboardNotFoundException();
            }
            EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
            if (ed == null) {
                LOGGER.debug("Dashboard not found with the specified id {}", dashboardId);
                throw new DashboardNotFoundException();
            }

            EmsUserOptions emsUserOptions = dsf.getEmsUserOptions(currentUser, dashboardId);
            if (emsUserOptions == null) {
                throw new UserOptionsNotFoundException();
            }

            return UserOptions.valueOf(emsUserOptions);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void saveOrUpdateUserOptions(UserOptions userOptions, Long tenantId) throws DashboardException {
        if (userOptions == null) {
            return;
        }
        EntityManager em = null;
        try {
            String currentUser = UserContext.getCurrentUser();
            DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
            em = dsf.getEntityManager();

            Long dashboardId = userOptions.getDashboardId();
            if (dashboardId == null || dashboardId <= 0) {
                LOGGER.debug("Dashboard not found for id {} is invalid", dashboardId);
                throw new DashboardNotFoundException();
            }
            EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
            if (ed == null) {
                LOGGER.debug("Dashboard not found with the specified id {}", dashboardId);
                throw new DashboardNotFoundException();
            }else{
                // create or update if exists
                EmsUserOptions emsUserOptions = dsf.getEmsUserOptions(currentUser, userOptions.getDashboardId());
                if(null == emsUserOptions){
                    dsf.persistEmsUserOptions(userOptions.toEntity(null, currentUser));
                }else{
                    emsUserOptions = userOptions.toEntity(emsUserOptions, currentUser);
                    dsf.mergeEmsUserOptions(emsUserOptions);
                }
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
