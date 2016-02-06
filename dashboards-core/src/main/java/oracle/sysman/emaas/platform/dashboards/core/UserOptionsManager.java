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

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsAlreadyExistException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

import javax.persistence.EntityManager;

/**
 * @author jishshi
 */
public class UserOptionsManager {
    private static final UserOptionsManager instance = new UserOptionsManager();

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

    public void updateUserOptions(UserOptions userOptions, Long tenantId) throws DashboardException {
        if (userOptions == null) {
            return;
        }
        EntityManager em = null;
        boolean isExist = false;
        try {
            String currentUser = UserContext.getCurrentUser();
            DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
            em = dsf.getEntityManager();

            EmsUserOptions emsUserOptions = null;
            if (userOptions.getDashboardId() != null) {
                emsUserOptions = dsf.getEmsUserOptions(currentUser, userOptions.getDashboardId());
                if (emsUserOptions != null) {
                    isExist = true;
                }
            }

            if (isExist) {
                // update
                emsUserOptions = userOptions.toEntity(emsUserOptions, currentUser);
                dsf.mergeEmsUserOptions(emsUserOptions);
            }else{
                throw new UserOptionsNotFoundException();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void saveUserOptions(UserOptions userOptions, Long tenantId) throws DashboardException {
        if (userOptions == null) {
            return;
        }
        EntityManager em = null;
        boolean isExist = false;
        try {
            String currentUser = UserContext.getCurrentUser();
            DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
            em = dsf.getEntityManager();

            EmsUserOptions emsUserOptions = null;
            if (userOptions.getDashboardId() != null) {
                emsUserOptions = dsf.getEmsUserOptions(currentUser, userOptions.getDashboardId());
                if (emsUserOptions != null) {
                    isExist = true;
                }
            }

            if (!isExist) {
                // create
                emsUserOptions = userOptions.toEntity(emsUserOptions, currentUser);
                dsf.persistEmsUserOptions(emsUserOptions);
            }else{
                throw new UserOptionsAlreadyExistException();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
