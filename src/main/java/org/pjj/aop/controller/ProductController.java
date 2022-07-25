package org.pjj.aop.controller;

import org.pjj.aop.annotation.M;
import org.springframework.stereotype.Component;

/**
 * @author PengJiaJun
 * @Date 2022/07/25 16:49
 */
@Component
public class ProductController {

    @M
    public double sellProduct() {
        System.out.println("购买了一件勇闯天涯(12瓶)");
        return 28.8;
    }
    @M
    public double sellProduct2() {
        System.out.println("购买了一个iPhone13");
        return 5888;
    }
}
