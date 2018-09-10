/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.service.external;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.gluu.oxtrust.model.GluuCustomPerson;
import org.xdi.model.SimpleCustomProperty;
import org.xdi.model.custom.script.CustomScriptType;
import org.xdi.model.custom.script.conf.CustomScriptConfiguration;
import org.xdi.model.custom.script.type.user.CacheRefreshType;
import org.xdi.service.custom.script.ExternalScriptService;
import org.xdi.util.StringHelper;

/**
 * Provides factory methods needed to create external cache refresh extension
 * 
 * @author Yuriy Movchan Date: 01/12/2015
 */
@ApplicationScoped
@Named("externalCacheRefreshService")
public class ExternalCacheRefreshService extends ExternalScriptService {

	private static final long serialVersionUID = 1707751544454591273L;

	public ExternalCacheRefreshService() {
		super(CustomScriptType.CACHE_REFRESH);
	}

	public boolean executeExternalUpdateUserMethod(CustomScriptConfiguration customScriptConfiguration, GluuCustomPerson user) {
		try {
			log.debug("Executing python 'updateUser' method");
			CacheRefreshType externalType = (CacheRefreshType) customScriptConfiguration.getExternalType();
			Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
			return externalType.updateUser(user, configurationAttributes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
            saveScriptError(customScriptConfiguration.getCustomScript(), ex);
		}

		return false;
	}

    public String executeExternalGetBindCredentialsMethod(CustomScriptConfiguration customScriptConfiguration, String configId) {
        try {
            log.debug("Executing python 'getBindCredentialsMethod' method");
            CacheRefreshType externalType = (CacheRefreshType) customScriptConfiguration.getExternalType();
            Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
            
            // Execute only if API > 1
            if (externalType.getApiVersion() > 1) {
                return externalType.getBindCredentials(configId, configurationAttributes);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            saveScriptError(customScriptConfiguration.getCustomScript(), ex);
        }

        return null;
    }

	public boolean executeExternalUpdateUserMethods(GluuCustomPerson user) {
		boolean result = true;
		for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
			result &= executeExternalUpdateUserMethod(customScriptConfiguration, user);
			if (!result) {
				return result;
			}
		}

		return result;
	}

    public String executeExternalGetBindCredentialsMethods(String configId) {
        String result = null;
        for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
            result = executeExternalGetBindCredentialsMethod(customScriptConfiguration, configId);
            if (StringHelper.isNotEmpty(result)) {
                return result;
            }
        }

        return result;
    }

}
