package com.imooc.mall2.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Shipping {//收货地址
    private Integer id;

    private Integer userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;


}