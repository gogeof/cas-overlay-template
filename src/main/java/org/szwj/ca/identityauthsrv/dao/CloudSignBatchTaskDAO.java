package org.szwj.ca.identityauthsrv.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignBatchTaskEntity;

import java.util.List;

@Repository
public interface CloudSignBatchTaskDAO {

    // 插入一行新数据
    @Insert(
        "INSERT INTO TLK_CLOUDSIGN_BATCHTASK(ID, BusinessSystemCode, Account, PreSignIDs, SignTitle, "
            + "Remark, NotifyUrl, BatchSignID) "
            + "VALUES(#{cloudSignBatchTaskEntity.ID},#{cloudSignBatchTaskEntity.BusinessSystemCode},"
            + "#{cloudSignBatchTaskEntity.Account},#{cloudSignBatchTaskEntity.PreSignIDs},"
            + "#{cloudSignBatchTaskEntity.SignTitle},#{cloudSignBatchTaskEntity.Remark},"
            + "#{cloudSignBatchTaskEntity.NotifyUrl},#{cloudSignBatchTaskEntity.BatchSignID})")
    void InsertBatchTaskRecord(
            @Param("cloudSignBatchTaskEntity") CloudSignBatchTaskEntity cloudSignBatchTaskEntity);

    // 查询所有用户
    @Select("SELECT * FROM TLK_CLOUDSIGN_BATCHTASK")
    @ResultType(CloudSignBatchTaskEntity.class)
    List<CloudSignBatchTaskEntity> QueryAllBatchTask();

    // 更新任务状态
    @Update(
        "UPDATE TLK_CLOUDSIGN_BATCHTASK SET SignResult = #{cloudSignBatchTaskEntity.SignResult}, "
            + " LastTime = NOW() WHERE ID = #{cloudSignBatchTaskEntity.ID}")
    void UpdateSignResultByID(
            @Param("cloudSignBatchTaskEntity") CloudSignBatchTaskEntity cloudSignBatchTaskEntity);

    // 更新任务执行结果
    @Update(
        "UPDATE TLK_CLOUDSIGN_BATCHTASK SET FinishTimeStr = #{finishTimeStr}, "
            + "SignStatus = #{signStatus}, SignResult = #{signResult}, LastTime = NOW() "
            + "WHERE BatchSignID = #{batchSignID}")
    Integer UpdateBatchSignResult(@Param("finishTimeStr") String finishTimeStr,
                                  @Param("signStatus") Integer signStatus, @Param("signResult") Integer signResult,
                                  @Param("batchSignID") String batchSignID);

    @Select("SELECT NotifyUrl FROM TLK_CLOUDSIGN_BATCHTASK WHERE BatchSignID = #{batchSignID}")
    @ResultType(String.class)
    String QueryNotifyUrlByBatchSignID(String batchSignID);
}
