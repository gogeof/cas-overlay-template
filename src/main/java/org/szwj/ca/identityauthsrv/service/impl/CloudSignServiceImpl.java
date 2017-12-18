package org.szwj.ca.identityauthsrv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.szwj.ca.identityauthsrv.dao.CloudSignBatchTaskDAO;
import org.szwj.ca.identityauthsrv.dao.CloudSignInfoDAO;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignBatchTaskEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignInfoEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.Files;
import org.szwj.ca.identityauthsrv.service.intfc.CloudSignService;

import java.util.List;

@Service("cloudSignService")
public class CloudSignServiceImpl implements CloudSignService {

    private final CloudSignInfoDAO cloudSignInfoDAO;

    private final CloudSignBatchTaskDAO cloudSignBatchTaskDAO;

    @Autowired
    public CloudSignServiceImpl(CloudSignInfoDAO cloudSignInfoDAO,
        CloudSignBatchTaskDAO cloudSignBatchTaskDAO) {
        this.cloudSignInfoDAO = cloudSignInfoDAO;
        this.cloudSignBatchTaskDAO = cloudSignBatchTaskDAO;
    }

    // table TLK_CLOUDSIGN_INFO
    @Override
    public void InsertCloudSignInfo(CloudSignInfoEntity cloudSignInfoEntity) {
        cloudSignInfoDAO.InsertCloudSignInfo(cloudSignInfoEntity);
    }

    @Override
    public Integer UpdateCloudSignInfoResult(Integer signStatus, String batchSignID, String filedId,
        String outFileId) {
        return cloudSignInfoDAO
            .UpdateCloudSignInfoResult(signStatus, batchSignID, filedId, outFileId);
    }

    @Override
    public Integer UpdateCloudSignInfoStatus(Integer signStatus, String outFileId) {
        return cloudSignInfoDAO.UpdateCloudSignInfoStatus(signStatus, outFileId);
    }

    @Override
    public List<CloudSignInfoEntity> QuerySignInfoByStatus(Integer signStatus) {
        return cloudSignInfoDAO.QuerySignInfoByStatus(signStatus);
    }


    // table TLK_CLOUDSIGN_BATCHTASK
    @Override
    public void InsertBatchTaskRecord(CloudSignBatchTaskEntity cloudSignBatchTaskEntity) {
        cloudSignBatchTaskDAO.InsertBatchTaskRecord(cloudSignBatchTaskEntity);
    }

    @Override
    public List<CloudSignBatchTaskEntity> QueryAllBatchTask() {
        return cloudSignBatchTaskDAO.QueryAllBatchTask();
    }

    @Override
    public void UpdateSignResultByID(CloudSignBatchTaskEntity cloudSignBatchTaskEntity) {
        cloudSignBatchTaskDAO.UpdateSignResultByID(cloudSignBatchTaskEntity);
    }

    @Override
    public Integer UpdateBatchSignResult(String finishTimeStr, Integer signStatus,
        Integer signResult, String batchSignID) {
        return cloudSignBatchTaskDAO
            .UpdateBatchSignResult(finishTimeStr, signStatus, signResult, batchSignID);
    }

    @Override
    public String QueryNotifyUrlByBatchSignID(String batchSignID) {
        return cloudSignBatchTaskDAO.QueryNotifyUrlByBatchSignID(batchSignID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public void UpdateCloudSignResult(String finishTimeStr, Integer signStatus, Integer signResult,
        String batchSignID, List<Files> filesList) {
        cloudSignBatchTaskDAO
            .UpdateBatchSignResult(finishTimeStr, signStatus, signResult, batchSignID);
        for (Files files : filesList) {
            cloudSignInfoDAO
                .UpdateCloudSignInfoResult(STATUS_OF_SUCCESS, batchSignID, files.getFiledId(),
                    files.getOutFileId());
        }
    }
}
