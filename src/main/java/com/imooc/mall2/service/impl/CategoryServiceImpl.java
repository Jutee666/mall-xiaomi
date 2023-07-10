package com.imooc.mall2.service.impl;

import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.dao.CategoryMapper;
import com.imooc.mall2.pojo.Category;
import com.imooc.mall2.service.ICategoryService;
import com.imooc.mall2.vo.CategoryVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: CategoryServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/20 21:41
 * @Version 1.0
 * Description:
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    //耗时：http（请求微信api）>磁盘>内存
    //mysql(内网+磁盘）
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
       // List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();
        //查处parent_id=0
//        for (Category category : categories) {
//            if (category.getParentId().equals(MallConst.ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }
        //lambda+stream
        List<CategoryVo> categoryVoList=categories.stream()
                //使用filter方法筛选出parentId等于特定值（在这里是MallConst.ROOT_PARENT_ID）的分类
                .filter(e->e.getParentId().equals(MallConst.ROOT_PARENT_ID))
                //然后使用map方法将每个Category对象转换为对应的CategoryVo对象
                .map(this::category2CategoryVo)
                //通过sorted方法按照sortOrder字段进行降序排序
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                //使用collect方法将结果收集到一个新的List对象中
                .collect(Collectors.toList());
        //查询子目录
        findSubCategory(categoryVoList,categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        /*for (Category category : categories) {
            if (category.getParentId().equals(id)){//如果子目录id等下根目录
                resultSet.add(category.getId());

                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }*///遍历数据库写到下面方法只需遍历一次
        findSubCategoryId(id,resultSet,categories);
    }
    //使用了递归。该方法用于查找给定根目录下的所有子目录的ID，并将其添加到resultSet集合中,不需要每次都查询数据库
    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories){//resultSet参数用于存储查找到的子分类的id集合
        for (Category category : categories) {
            if (category.getParentId().equals(id)){//如果子目录id等于根目录
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }
    //使用了递归。该方法用于查找并设置子分类
    private void findSubCategory( List<CategoryVo> categoryVoList,List<Category> categories){
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList=new ArrayList<>();//子目录
            for (Category category : categories) {//遍历整个数据库
                //在整个分类列表categories中查找与当前分类的id匹配的子分类。
                // 如果找到匹配的子分类，将其转换为CategoryVo对象并添加到subCategoryVoList中
                if (categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                findSubCategory(subCategoryVoList,categories);
            }
        }
    }
    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
