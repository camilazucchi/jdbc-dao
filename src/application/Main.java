package application;

import model.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("---- TEST #1: seller findById method ----");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println();
        System.out.println("---- TEST #2: seller findByDepartment method ----");
        Department dep = new Department(1, null);
        List<Seller> sellers = sellerDao.findByDepartment(dep);
        sellers.forEach(System.out::println);

    }
}