package com.studing.cashRegister.dao;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.util.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class GoodsDaoTest {
    private static Connection connection = null;
    private GoodsDao goodsDao = GoodsDao.getInstance();
    private static List<Goods> savedGoods = new ArrayList<>();

    static{
        Properties props = new Properties();
        String url = "url";
        String userName = "userName";
        String password = "password";
        try (InputStream input = UserDaoTest.class.getClassLoader().getResourceAsStream("config.properties");) {
            props.load(input);
            url = props.getProperty("db.url");
            userName = props.getProperty("db.username");
            password = props.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static{
        for (int i = 0; i < 3; i++) {
            savedGoods.add(new Goods(0, "myGoods"+i, "desc"+i, i*100.4, i));
        }
    }

    @BeforeAll
    static void beforeAll() throws MyException {
        PreparedStatement statement = null;
        Statement statement1 = null;
        try {
            long firstId = savedGoods.size();
            String sql = "DELETE FROM goods";
            statement1 = connection.createStatement();
            statement1.executeUpdate(sql);
            sql = "INSERT INTO goods (name, description, price, quantity) VALUES (?,?,?,?), (?,?,?,?), (?,?,?,?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < 3; i++) {
                statement.setString(i*4+1, savedGoods.get(i).getName());
                statement.setString(i*4+2, savedGoods.get(i).getDescription());
                statement.setDouble(i*4+3, savedGoods.get(i).getPrice());
                statement.setInt(i*4+4, savedGoods.get(i).getQuantity());
            }
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                firstId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating goods failed, no ID obtained.");
            }
            for (int i = 0; i < 3; i++) {
                savedGoods.get(i).setId(firstId+i);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MyException("Cannot save user data.", throwables);
        }finally {
            ConnectionPool.close(statement1);
            ConnectionPool.close(statement);
        }
    }

    @Test
    void getInstance() {
        GoodsDao goodsDao1 = GoodsDao.getInstance();
        GoodsDao goodsDao2 = GoodsDao.getInstance();
        assertEquals(goodsDao1, goodsDao2);
        assertSame(goodsDao1, goodsDao2);
    }

    @Test
    void getAll() throws MyException {
        List<Goods> goodsFromDb = goodsDao.getAll(connection);
        assertTrue(goodsFromDb.size()==savedGoods.size());
        for (int i = 0; i < goodsFromDb.size(); i++) {
            assertEquals(goodsFromDb.get(i).getName(), savedGoods.get(i).getName());
        }
    }

    @Test
    void getById() throws MyException {
        Goods goods = goodsDao.getById(connection, savedGoods.get(0).getId());
        assertEquals(goods.getName(), savedGoods.get(0).getName());
    }

    @Test
    void getByName() throws MyException {
        Goods goods = goodsDao.getByName(connection, savedGoods.get(0).getName());
        assertEquals(goods.getName(), savedGoods.get(0).getName());
    }

    @Test
    void save() throws MyException {
        Goods goods = new Goods(0, "newGoods", "desc", 12.3, 50);
        goodsDao.save(connection, goods);
        Goods goodsFromDb = goodsDao.getByName(connection, goods.getName());
        assertEquals(goods.getName(), goodsFromDb.getName());
    }

    @Test
    void update() throws MyException {
        savedGoods.get(0).setDescription("newDesc");
        savedGoods.get(0).setPrice(0.5);
        savedGoods.get(0).setQuantity(10);
        goodsDao.update(connection, savedGoods.get(0));
        Goods goods = goodsDao.getById(connection, savedGoods.get(0).getId());
        assertEquals(goods.getId(), savedGoods.get(0).getId());
        assertEquals(goods.getDescription(), savedGoods.get(0).getDescription());
        assertEquals(goods.getPrice(), savedGoods.get(0).getPrice());
        assertEquals(goods.getQuantity(), savedGoods.get(0).getQuantity());
    }

    @Test
    void getGoodsNamesByIds() throws MyException {
        List<String> names = List.of(savedGoods.get(0).getName(), savedGoods.get(1).getName());
        List<Long> ids = List.of(savedGoods.get(0).getId(), savedGoods.get(1).getId());
        List<String> namesFromDb = goodsDao.getGoodsNamesByIds(connection, ids);
        assertEquals(names.size(), namesFromDb.size());
        assertTrue(names.containsAll(namesFromDb));
        assertTrue(namesFromDb.containsAll(names));
    }
}