package com.imooc.mall2.service;

import com.imooc.mall2.form.CartAddForm;
import com.imooc.mall2.form.CartUpdateForm;
import com.imooc.mall2.pojo.Cart;
import com.imooc.mall2.vo.CartVo;
import com.imooc.mall2.vo.ResponseVo;

import java.util.List;

/**
 * ClassName: ICartService
 * Package: com.imooc.mall2.service
 *
 * @Author 马学兴
 * @Create 2023/6/21 23:31
 * @Version 1.0
 * Description:
 */
public interface ICartService {
    ResponseVo<CartVo> add(Integer uid,CartAddForm form);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);
    ResponseVo<CartVo> selectAll(Integer uid);
    ResponseVo<CartVo> unselectAll(Integer uid);
    ResponseVo<Integer> sum(Integer uid);
    List<Cart> listForCart(Integer uid);

}
