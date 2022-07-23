package org.pjj.proxy.dynamic.factory;

import org.pjj.proxy.dynamic.service.UsbSell;

/**
 * 金士顿厂家, 不接受用户的单独购买
 * 目标对象, 即需要代理的对象
 * @author PengJiaJun
 * @Date 2022/07/23 01:26
 */
public class UsbKingFactory implements UsbSell {
    /**
     * 购买 amount个U盘需要的钱
     * @param amount 表示 购买 amount个U盘
     * @return 返回 购买 amount 个U盘 需要的钱
     */
    @Override
    public float sell(int amount) {
        //一个 128G 的U盘是 85元。
        float unitPrice = 85.0f;
        //后期根据amount，可以实现不同的价格，例如10000个，单价80，50000个单价75
        if(amount >= 50000) {
            return amount * 75.0f;
        }else if(amount >= 10000) {
            return amount * 80.0f;
        }else {
            return amount * 85.0f;
        }
    }

    @Override
    public void sayHello() {
        System.out.println("UsbKingFactory对你说hello                 (目标对象对你说hello)");
    }
}
