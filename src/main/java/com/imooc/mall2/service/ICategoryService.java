package com.imooc.mall2.service;

import com.imooc.mall2.pojo.Category;
import com.imooc.mall2.vo.CategoryVo;
import com.imooc.mall2.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * ClassName: ICategoryService
 * Package: com.imooc.mall2.service
 *
 * @Author 马学兴
 * @Create 2023/6/20 21:40
 * @Version 1.0
 * Description:
 */
public interface ICategoryService {
    ResponseVo<List<CategoryVo>> selectAll();
    void findSubCategoryId(Integer id, Set<Integer> resultSet);//子类和子子类的集合
}
