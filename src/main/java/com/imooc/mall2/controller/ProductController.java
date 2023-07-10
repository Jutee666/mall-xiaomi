package com.imooc.mall2.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall2.service.IProductService;
import com.imooc.mall2.vo.ProductDetailVo;
import com.imooc.mall2.vo.ResponseVo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ProductController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/21 14:00
 * @Version 1.0
 * Description:
 */
@RestController
public class ProductController {
    @Autowired
    private IProductService productService;
    @GetMapping("/products")
    //根据目录id查看商品列表
    public ResponseVo<PageInfo> list(@RequestParam (required = false)Integer categoryId,
                                     @RequestParam(required = false,defaultValue="1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue="10") Integer pageSize){
        return productService.list(categoryId, pageNum,pageSize);
    }
    //根据商品id查看详情
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId){
        return productService.detail(productId);
    }
}
