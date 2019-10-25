package com.sequoiacode.api.config;

public enum WASEnv {
    WAS_DEV("https://stx-was9-dmgr-dev.mud.internal.co.za:9043/ibm/console/logon.jsp"),
    WAS_TST("https://localhost:9043/ibm/console/logon.jsp"),
    WAS_PRO("https://localhost:9043/ibm/console/logon.jsp"),
    WAS_NOT_LOGGED_IN("ibm/console/secure/securelogon.do");
    public final String label;

    private WASEnv(String label) {
        this.label = label;
    }
}
