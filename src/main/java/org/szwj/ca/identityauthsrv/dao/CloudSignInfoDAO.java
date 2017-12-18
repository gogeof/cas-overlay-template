package org.szwj.ca.identityauthsrv.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignInfoEntity;

import java.util.List;

@Repository
public interface CloudSignInfoDAO {

    // 插入一行新数据
    @Insert("INSERT INTO TLK_CLOUDSIGN_INFO(ID, BusinessSystemCode, Data, PdfBasePath, "
        + "PdfRelativePath, BusinessOrgCode, PreSignID) "
        + "VALUES(#{cloudSignInfoEntity.ID},#{cloudSignInfoEntity.BusinessSystemCode},"
        + "#{cloudSignInfoEntity.Data},#{cloudSignInfoEntity.PdfBasePath},"
        + "#{cloudSignInfoEntity.PdfRelativePath},#{cloudSignInfoEntity.BusinessOrgCode},"
        + "#{cloudSignInfoEntity.PreSignID})")
    void InsertCloudSignInfo(@Param("cloudSignInfoEntity") CloudSignInfoEntity cloudSignInfoEntity);

    // 更新签署信息执行结果
    @Update("UPDATE TLK_CLOUDSIGN_INFO SET SignStatus = #{signStatus}, BatchSignID = #{batchSignID}, "
        + "FiledId = #{filedId}, LastTime = NOW() WHERE ID = #{outFileId}")
    Integer UpdateCloudSignInfoResult(@Param("signStatus") Integer signStatus,
                                      @Param("batchSignID") String batchSignID, @Param("filedId") String filedId,
                                      @Param("outFileId") String outFileId);

    // 更新签署信息状态
    @Update("UPDATE TLK_CLOUDSIGN_INFO SET SignStatus=#{signStatus}, LastTime=NOW() "
        + "WHERE ID=#{outFileId}")
    Integer UpdateCloudSignInfoStatus(@Param("signStatus") Integer signStatus,
                                      @Param("outFileId") String outFileId);

    // 查询所有用户
    @Select("SELECT * FROM TLK_CLOUDSIGN_INFO WHERE SignStatus = #{signStatus}")
    @ResultType(CloudSignInfoEntity.class)
    List<CloudSignInfoEntity> QuerySignInfoByStatus(@Param("signStatus") Integer signStatus);
}
