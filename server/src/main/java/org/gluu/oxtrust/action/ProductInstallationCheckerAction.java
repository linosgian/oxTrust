/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2016, Gluu
 */
package org.gluu.oxtrust.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gluu.oxtrust.ldap.service.Shibboleth3ConfService;
import org.gluu.oxtrust.util.ProductInstallationChecker;
import org.gluu.service.security.Secure;
import org.slf4j.Logger;

/**
 * Action class for updating and adding the SAML IDP to Asimba.
 * 
 * @author Dmitry Ognyannikov
 */
@ApplicationScoped
@Named
@Secure("#{identity.loggedIn}")
public class ProductInstallationCheckerAction implements Serializable {

	private static final long serialVersionUID = 1125167091541923404L;

	@Inject
	private Logger log;

	@Inject
	private Shibboleth3ConfService shibboleth3ConfService;

	private boolean showSAMLMenu = true;
	private boolean showSAMLSubmenu = true;
	private boolean showIDP_CAS = true;
	private boolean showPassportMenu = true;

	public ProductInstallationCheckerAction() {
	}

	@PostConstruct
	public void init() {
		log.debug("init() ProductInstallationCheckerAction call");
		showSAMLMenu = !ProductInstallationChecker.isGluuCE() || ProductInstallationChecker.isOxAsimbaInstalled()
				|| (shibboleth3ConfService.isIdpInstalled() && ProductInstallationChecker.isShibbolethIDP3Installed());
		showSAMLSubmenu = !ProductInstallationChecker.isGluuCE() || shibboleth3ConfService.isIdpInstalled();
		showIDP_CAS = !ProductInstallationChecker.isGluuCE() || ProductInstallationChecker.isShibbolethIDP3Installed();
		showPassportMenu = !ProductInstallationChecker.isGluuCE() || ProductInstallationChecker.isPassportInstalled();
	}

	public boolean isShowSAMLMenu() {
		return showSAMLMenu;
	}

	public void setShowSAMLMenu(boolean showSAMLMenu) {
		this.showSAMLMenu = showSAMLMenu;
	}

	public boolean isShowSAMLSubmenu() {
		return showSAMLSubmenu;
	}

	public void setShowSAMLSubmenu(boolean showSAMLSubmenu) {
		this.showSAMLSubmenu = showSAMLSubmenu;
	}

	public boolean isShowIDP_CAS() {
		return showIDP_CAS;
	}

	public void setShowIDP_CAS(boolean showIDP_CAS) {
		this.showIDP_CAS = showIDP_CAS;
	}

	public boolean isShowPassportMenu() {
		return showPassportMenu;
	}

	public void setShowPassportMenu(boolean showPassportMenu) {
		this.showPassportMenu = showPassportMenu;
	}

}
