package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignBatchTaskEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignInfoEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.Files;

import java.util.List;

public interface CloudSignService {

    // 签署成功
    public static Integer STATUS_OF_SUCCESS = 1;

    // 签署失败
    public static Integer STATUS_OF_FAIL = 2;

    // 下载成功
    public static Integer STATUS_OF_DOWNLOAD = 3;

    // table TLK_CLOUDSIGN_INFO
    void InsertCloudSignInfo(CloudSignInfoEntity cloudSignInfoEntity);

    Integer UpdateCloudSignInfoResult(Integer signStatus, String batchSignID, String filedId,
                                      String outFileId);

    Integer UpdateCloudSignInfoStatus(Integer signStatus, String outFileId);

    List<CloudSignInfoEntity> QuerySignInfoByStatus(Integer signStatus);

    // table TLK_CLOUDSIGN_BATCHTASK
    void InsertBatchTaskRecord(CloudSignBatchTaskEntity cloudSignBatchTaskEntity);

    List<CloudSignBatchTaskEntity> QueryAllBatchTask();

    void UpdateSignResultByID(CloudSignBatchTaskEntity cloudSignBatchTaskEntity);

    Integer UpdateBatchSignResult(String finishTimeStr, Integer signStatus, Integer signResult,
                                  String batchSignID);

    String QueryNotifyUrlByBatchSignID(String batchSignID);

    // both TLK_CLOUDSIGN_INFO and TLK_CLOUDSIGN_BATCHTASK
    void UpdateCloudSignResult(String finishTimeStr, Integer signStatus, Integer signResult,
                               String batchSignID, List<Files> files);
}
