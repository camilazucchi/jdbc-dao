package application;

import model.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("---- TEST #1: seller findById method ----");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

    }
}