package com.imooc.mall2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall2.dao.ShippingMapper;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.form.ShippingForm;
import com.imooc.mall2.pojo.Shipping;
import com.imooc.mall2.service.IShippintService;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ShippintServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/22 20:53
 * @Version 1.0
 * Description:
 */
@Service
public class ShippintServiceImpl implements IShippintService {

    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ResponseVo<Map<String,Integer>> add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form,shipping);
        shipping.setUserId(uid);
        int row = shippingMapper.insertSelective(shipping);
        if (row==0){
            return  ResponseVo.error(ResponserEnem.ERROR);
        }
        Map<String,Integer> map=new HashMap<>();
        map.put("shippingId",shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row==0){
            return  ResponseVo.error(ResponserEnem.DELETE_SHIPPING_FAIL);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form,shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row==0){
            return  ResponseVo.error(ResponserEnem.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo=new PageInfo(shippings);

        return ResponseVo.success(pageInfo);
    }
}
