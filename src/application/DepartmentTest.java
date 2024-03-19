package application;

import model.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentTest {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println();
        System.out.println("---- TEST #7: department findById method ----");
        Department department = departmentDao.findById(3);
        System.out.println(department);


    }
}
