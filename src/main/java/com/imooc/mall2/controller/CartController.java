package com.imooc.mall2.controller;

import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.form.CartAddForm;
import com.imooc.mall2.form.CartUpdateForm;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.service.ICartService;
import com.imooc.mall2.vo.CartVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * ClassName: CartController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/21 22:41
 * @Version 1.0
 * Description:
 */
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list( HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm, HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(),cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(),productId,form);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(),productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }
    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unselectAll(HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unselectAll(user.getId());
    }
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session) {
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }

}
