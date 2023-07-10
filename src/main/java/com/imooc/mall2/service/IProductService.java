package com.imooc.mall2.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall2.vo.ProductDetailVo;
import com.imooc.mall2.vo.ResponseVo;


/**
 * ClassName: IProductService
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/21 11:14
 * @Version 1.0
 * Description:
 */
public interface IProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);
    ResponseVo<ProductDetailVo> detail(Integer productId);

}
