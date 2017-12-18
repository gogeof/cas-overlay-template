package sglca.helper.utils.common;

import sglca.helper.SZWJ_CaHelper;

public class ParameterCheckUtil {

    public static Boolean isCertAuthorityValid(String certAuthority) {
        return SZWJ_CaHelper.NETCA_AUTHORITY.equals(certAuthority) || SZWJ_CaHelper.BJCA_AUTHORITY
            .equals(certAuthority);
    }
}
