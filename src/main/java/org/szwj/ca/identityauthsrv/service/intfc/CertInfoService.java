package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;

/**
 * CertInfoService 接口类
 */
public interface CertInfoService {

    /**
     * 根据SN码获取证书颁发机构Authority
     *
     * @param sn 证书sn码
     * @return 颁发机构Authority
     */
    String QueryAuthorityBySn(String sn);

    /**
     * 根据SN码获取证书信息实体类
     *
     * @param sn 证书sn码
     * @return 证书信息实体类
     */
    CertInfoEntity QueryCertInfoBySn(String sn);
}
