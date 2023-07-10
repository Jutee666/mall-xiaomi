package com.imooc.mall2.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall2.form.ShippingForm;
import com.imooc.mall2.vo.ResponseVo;

import java.util.Map;

/**
 * ClassName: IShippintService
 * Package: com.imooc.mall2.service
 *
 * @Author 马学兴
 * @Create 2023/6/22 20:47
 * @Version 1.0
 * Description:
 */
public interface IShippintService {
    ResponseVo<Map<String,Integer>>add(Integer uid, ShippingForm form);
    ResponseVo delete(Integer uid,Integer shippingId);
    ResponseVo update(Integer uid,Integer shippingId,ShippingForm form);
    ResponseVo<PageInfo> list(Integer uid,Integer pageNum, Integer pageSize);

}
