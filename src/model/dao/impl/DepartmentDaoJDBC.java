package model.dao.impl;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO department " +
                "(name) " +
                "VALUES (?) ",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {
        try (PreparedStatement st = conn.prepareStatement("UPDATE department " +
                "SET name = ? " +
                "WHERE id = ?")) {
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM department " +
                "WHERE id = ?")) {
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Department with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try (PreparedStatement st = conn.prepareStatement("SELECT * " +
                "FROM department " +
                "WHERE id = ?")) {
            st.setInt(1, id);
            return executeQueryAndProcessResult(st);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Department executeQueryAndProcessResult(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return instantiateDepartment(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private List<Department> executeQueryAndProcessMultiple(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            List<Department> departments = new ArrayList<>();

            while (rs.next()) {
                Department obj = instantiateDepartment(rs);
                departments.add(obj);
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("id"));
        dep.setName(rs.getString("name"));
        return dep;
    }

    @Override
    public List<Department> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM department ORDER BY name")) {
            return executeQueryAndProcessMultiple(st);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
