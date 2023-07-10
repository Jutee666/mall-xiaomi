package com.imooc.mall2.vo;

import lombok.Data;

import java.util.List;

/**
 * ClassName: CategoryVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/20 21:24
 * @Version 1.0
 * Description:
 */
@Data
public class CategoryVo {
    private Integer id;

    private Integer parentId;
    private String name;
    private Integer sortOrder;
    private List<CategoryVo> subCategories;
}
