package org.pjj.proxy.staticc.service;

/**
 * 表示功能, 商家, 厂家 都要完成的功能 即 代理, 厂家都要完成的功能
 * @author PengJiaJun
 * @Date 2022/07/22 22:10
 */
public interface UsbSell {
    /**
     * 购买 amount个U盘需要的钱
     * @param amount 表示 购买 amount个U盘
     * @return 返回 购买 amount 个U盘 需要的钱
     */
    float sell(int amount);
}
