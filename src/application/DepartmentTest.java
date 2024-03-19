package application;

import model.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class DepartmentTest {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Scanner sc = new Scanner(System.in);

        // (✓) TEST #1 - findById()
        System.out.println();
        System.out.println("---- TEST #1: department findById method ----");
        Department department = departmentDao.findById(3);
        System.out.println(department);

        // (✓) TEST #2 - findAll()
        System.out.println();
        System.out.println("---- TEST #2: department findAll method ----");
        List<Department> departments = departmentDao.findAll();
        departments.forEach(System.out::println);

        // (✓) TEST #3 - insert()
        System.out.println();
        System.out.println("---- TEST #3: department insert method ----");
        Department newDepartment = new Department(null, "Toys");
        departmentDao.insert(newDepartment);
        System.out.println("Done! New id: " + newDepartment.getId());

        // (✓) TEST #4 - update()
        System.out.println();
        System.out.println("---- TEST #4: department update method ----");
        department = departmentDao.findById(6);
        department.setName("Cosmetics");
        departmentDao.update(department);
        System.out.println("Update completed!");

        // (✓) TEST #5 - deleteById()
        System.out.println();
        System.out.println("---- TEST #5: department deleteById method ----");
        System.out.print("Enter ID for TEST #5: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed!");




    }
}
