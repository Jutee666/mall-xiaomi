package com.imooc.mall2.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall2.dao.CategoryMapper;
import com.imooc.mall2.dao.ProductMapper;
import com.imooc.mall2.enums.ProductStatusEnum;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.pojo.Product;
import com.imooc.mall2.service.ICategoryService;
import com.imooc.mall2.service.IProductService;
import com.imooc.mall2.vo.ProductDetailVo;
import com.imooc.mall2.vo.ProductVo;
import com.imooc.mall2.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: ProductServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/21 11:16
 * @Version 1.0
 * Description:
 */
@Service
@Slf4j

public class ProductServiceImpl implements IProductService {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();//用于存储目录的 id
        if (categoryId != null) {//先查子类1
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

      /*  List<ProductVo> productVoList = new ArrayList<>();
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        for (Product e : productList) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(e, productVo);
            productVoList.add(productVo);
        }*/
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        //lambda表达式
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);//将商品对象 Product 转换为对应的商品视图对象 ProductVo
                    return productVo;
                })
                .collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);

    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //只对确定性条件判断
        if (product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode()) || product.getStatus().equals(ProductStatusEnum.DELETE.getCode())) {
            return ResponseVo.error(ResponserEnem.PRODUCT_OFF_SALE_OR_DELETE);
        }
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        //库存敏感处理
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return ResponseVo.success(productDetailVo);
    }
}
