package org.pjj.gof23.factory01;

/**
 * Car工厂
 * 有了工厂之后, 客户就不用依赖一堆车了, 只需要依赖工厂, 工厂内部来依赖这些车
 * 相当于给客户解耦了, 而专门通过一个工厂来维护与车之间的关系
 *
 * @author PengJiaJun
 * @Date 2022/08/04 23:16
 */
public class CarFactory {

    /**
     * 此处 参数 type 我觉得实际使用时, 从String类型换为 枚举类型应该会好一些
     * @param type
     * @return
     */
    public Car createCar(String type) {
        if("奥迪".equals(type)) {
            return new Audi();
        }else if("比亚迪".equals(type)) {
            return new Byd();
        } else {
            return null;
        }
        //简单工厂的缺点
        //新增产品时(Car), 需要修改工厂代码, 不符合开闭原则
//        else if ("新车".equals(type)) {return new 新车()}
    }


    //工厂除了想上面那种提供一个方法+参数来完成创建各种对象的功能外
    //还可以想这样提供具体创建哪种车的方法
    public Car createAudi() {
        return new Audi();
    }
    public Car createByd() {
        return new Byd();
    }

}
