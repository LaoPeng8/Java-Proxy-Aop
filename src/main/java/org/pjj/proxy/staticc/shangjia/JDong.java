package org.pjj.proxy.staticc.shangjia;

import org.pjj.proxy.staticc.factory.UsbKingFactory;
import org.pjj.proxy.staticc.service.UsbSell;

/**
 * 京东, 商家, 属于代理对象 代理 金士顿厂家
 *
 * @author PengJiaJun
 * @Date 2022/07/22 22:39
 */
public class JDong implements UsbSell {
    //声明 本商家 代理的厂家是谁
    private UsbKingFactory usbKingFactory = new UsbKingFactory();

    /**
     * 用户 调用代理对象JD的sell方法购买U盘
     * JD做为代理对象自己又没有U盘, 所以JD调用金士顿的sell购买U盘
     * 这波 就是 用户通过JD这个代理间接的访问了金士顿
     *
     * 那么JD代理对象，完全可以在 真正购买U盘前，后 做一些事情, 比如买之前打印日志，买之后生成 支付记录
     * 这就是 相当于 SpringAop的前置通知 后置通知  (不修改目标对象的基础上，增强主业务逻辑)
     *
     * @param amount 表示 购买 amount个U盘
     * @return
     */
    @Override
    public float sell(int amount) {
        float price = usbKingFactory.sell(amount);//进货的价格(金士顿的价格)

        float newPrice = (amount * 15 + price);//商家的价格 (每个U盘赚5块) (淘宝每个赚10块，京东每个赚15)
        System.out.println("京东物流, 提供物流保障, 退换货方便的一批");

        return newPrice;
    }
}
