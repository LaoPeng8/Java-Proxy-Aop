package org.pjj.transcation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author PengJiaJun
 * @Date 2022/08/01 19:00
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 转出
     * @param fromName
     * @param money
     */
    public void out(String fromName, Integer money) {
        String sql = "update spring_transcation set money = money - ? where name = ?";
        jdbcTemplate.update(sql, money, fromName);
    }

    public void in(String toName, Integer money) {
        String sql = "update spring_transcation set money = money + ? where name = ?";
        jdbcTemplate.update(sql, money, toName);
    }

}
