package com.imooc.mall2.controller;

import com.imooc.mall2.service.ICategoryService;
import com.imooc.mall2.vo.CategoryVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: CategoryController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/20 22:05
 * @Version 1.0
 * Description:
 */
@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> selectAll(){
        return categoryService.selectAll();
    }
}
