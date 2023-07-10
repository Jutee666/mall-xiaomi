package com.imooc.pay.dao;

import com.imooc.pay.pojo.PayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
@Mapper
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);

    PayInfo selectByOrderNo(Long orderNo);
}