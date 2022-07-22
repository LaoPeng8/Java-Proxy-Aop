package org.pjj.proxy.staticc;

import org.pjj.proxy.staticc.shangjia.JDong;
import org.pjj.proxy.staticc.shangjia.TaoBao;

/**
 * 客户
 * 客户通过 TaoBao间接的访问 金士顿
 * 客户 访问 TaoBao 访问 金士顿
 * 客户 访问  代理  访问  金士顿
 *
 * 金士顿U盘原价一个 85
 * 淘宝 在 金士顿的基础上每个 U盘 加价 10 块 变成 95 (也可以加其他的吗，比如买之前打印日志，买之后生成 支付记录)
 * 就相当于是 在 不修改目标对象的基础上(金士顿)，增强主业务逻辑
 *
 * 金士顿U盘原价一个 85
 * 京东 在 金士顿的基础上每个 U盘 加价 15 块 变成 100 (同时提供了JD物流的服务, 以及京东各种自家的服务)
 * 就相当于是 在 不修改目标对象的基础上(金士顿)，增强主业务逻辑
 *
 * @author PengJiaJun
 * @Date 2022/07/22 22:29
 */
public class ShopMain {

    public static void main(String[] args) {
        TaoBao taoBao = new TaoBao();
        float sell = taoBao.sell(10);
        System.out.println("淘宝买10个U盘需要花费: " + sell);

        JDong jDong = new JDong();
        float sell2 = jDong.sell(10);
        System.out.println("京东买10个U盘需要花费: " + sell2);
    }

}
