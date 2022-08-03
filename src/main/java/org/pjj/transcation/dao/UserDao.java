package org.pjj.transcation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

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
        //根据主键进行update, 避免死锁
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select id from spring_transcation where name = ?", fromName);
        jdbcTemplate.update("update spring_transcation set money = money - ? where id = ?", money, (Integer) stringObjectMap.get("id"));


//        String sql = "update spring_transcation set money = money - ? where name = ?";
//        jdbcTemplate.update(sql, money, fromName);
    }

    public void in(String toName, Integer money) {
        //根据主键进行update, 避免死锁
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select id from spring_transcation where name = ?", toName);
        jdbcTemplate.update("update spring_transcation set money = money + ? where id = ?", money, (Integer) stringObjectMap.get("id"));

//        String sql = "update spring_transcation set money = money + ? where name = ?";
//        jdbcTemplate.update(sql, money, toName);
    }

}
