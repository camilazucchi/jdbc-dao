package model.dao.impl;
/* O package impl conterá implementações do DAO */

import model.dao.SellerDao;
import model.entities.Seller;

import java.util.List;

/* Esta classe implementa a interface "SellerDao" e consequentemente todos os seus métodos */
public class SellerDaoJDBC implements SellerDao {

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        return null;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

}