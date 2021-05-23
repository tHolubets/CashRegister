package com.studing.cashRegister.service;

import com.studing.cashRegister.dao.GoodsDao;
import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.util.ConnectionPool;

import java.sql.Connection;
import java.util.List;

/**
 * Class for goods management. Singleton
 * @author tHolubets
 */
public class GoodsService {
    private GoodsDao goodsDao = GoodsDao.getInstance();
    private static GoodsService instance;

    /**
     * Method to get instance of Service (because of Singleton)
     * @return instance of GoodsService
     */
    public static synchronized GoodsService getInstance(){
        if(instance == null){
            instance = new GoodsService();
        }
        return instance;
    }

    private GoodsService(){}

    /**
     * Method to get all goods
     * @return list of goods
     * @throws MyException if the DAO class throw it
     */
    public List<Goods> getAll() throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Goods> result = goodsDao.getAll(connection);
        ConnectionPool.close(connection);
        return result;
    }

    /**
     * Method to get goods by its id
     * @param id goods id
     * @return goods object or null if missing
     * @throws MyException if the DAO class throw it
     */
    public Goods getById(long id) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Goods goods = goodsDao.getById(connection, id);
        ConnectionPool.close(connection);
        return goods;
    }

    /**
     * Method to save goods
     * @param goods object to save
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if the DAO class throw it
     */
    public boolean save(Goods goods) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        boolean result = false;
        if(goods.getId()!=0){
            result = goodsDao.update(connection, goods);
        }
        else {
            result = goodsDao.save(connection, goods);
        }
        ConnectionPool.close(connection);
        return result;
    }
}
