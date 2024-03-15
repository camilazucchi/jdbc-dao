package model.dao.impl;
/* O package impl conterá implementações do DAO */

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Esta classe implementa a interface "SellerDao" e consequentemente todos os seus métodos */
public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    /* Este construtor é utilizado para injetar uma instância de conexão com o banco de dados ("Connection") na classe
     * "SellerDaoJDBC". */
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
    }

    @Override
    public void update(Seller obj) {
    }

    @Override
    public void deleteById(Integer id) {
    }

    // Este método busca um vendedor (Seller) pelo seu ID no banco de dados:
    @Override
    public Seller findById(Integer id) {

        /* Modifiquei este método para utilizar try-with-resources para garantir que os recursos, como
         * PreparedStatement e ResultSet, sejam fechados corretamente, mesmo em caso de exceção.
         * Isso simplifica o código e elimina a necessidade de explicitamente fechar os recursos no bloco finally. */
        try (PreparedStatement st = conn.prepareStatement("SELECT seller.*, department.Name as DepName " +
                "FROM seller " +
                "INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE seller.Id = ?")) {
            st.setInt(1, id);
            return executeQueryAndProcessResult(st);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    // Este trecho também foi retirado do método "findById()":
    private Seller executeQueryAndProcessResult(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs, dep);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    /* Este é um método privado que recebe um objeto "PreparedStatement" como parâmetro e retorna uma lista de
    * vendedores: */
    private List<Seller> executeQueryAndProcessResultMultiple(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            // Cria uma lista vazia de vendedores:
            List<Seller> sellers = new ArrayList<>();
            /* Cria um map vazio que será usado para armazenar departamentos. O mapa associa IDs de departamento
            (inteiro) a objetos de departamento: */
            Map<Integer, Department> map = new HashMap<>();

            /* Inicia um loop enquanto houver linhas disponíveis no "ResultSet". Isso percorre cada linha retornada
            pela consulta: */
            while (rs.next()) {
                String departmentId = "departmentid";
                Department dep = map.get(rs.getInt(departmentId));
                // Caso o departamento não exista, ele será salvo no map:
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt(departmentId), dep);
                }

                sellers.add(instantiateSeller(rs, dep));
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    /* Separamos o trecho de código que utilizamos no método "findById()" para o método "instantiateDepartment()".
     * Isso ajuda a melhorar a legibilidade e a modularidade do nosso código. */
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        // Define o ID do departamento usando o valor da coluna "departmentid" no resultado da consulta:
        dep.setId(rs.getInt("departmentid"));
        // Define o nome do departamento usando o valor da coluna "depname" no resultado da consulta:
        dep.setName(rs.getString("depname"));
        return dep;
    }

    /* Este trecho de código também foi retirado do método "findById()" e inserido no método "instantiateSeller".
     * Isso ajuda a melhorar a legibilidade e a modularidade do nosso código. */
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        // Define o ID do vendedor usando o valor da coluna "id" no resultado da consulta:
        obj.setId(rs.getInt("id"));
        // Define o nome do vendedor usando o valor da coluna "name" no resultado da consulta:
        obj.setName(rs.getString("name"));
        // Define o e-mail do vendedor usando o valor da coluna "email" no resultado da consulta:
        obj.setEmail(rs.getString("email"));
        // Define a data de nascimento do vendedor usando o valor da coluna "birthdate" no resultado da consulta:
        obj.setBirthDate(rs.getDate("birthdate"));
        // Define o salário do vendedor usando o valor da coluna "basesalary" no resultado da consulta:
        obj.setBaseSalary(rs.getDouble("basesalary"));
        // Define o departamento do vendedor como o objeto "Department" criado anteriormente:
        obj.setDepartment(dep);
        // Retorna o objeto "Seller" preenchido com as informações recuperadas do banco de dados:
        return obj;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    // Este método retorna uma lista de vendedores de um departamento específico:
    @Override
    public List<Seller> findByDepartment(Department department) {
        try (PreparedStatement st = conn.prepareStatement("SELECT seller.*, department.Name as DepName " +
                "FROM seller " +
                "INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE DepartmentId = ? " +
                "ORDER BY Name")) {
            st.setInt(1, department.getId());
            return executeQueryAndProcessResultMultiple(st);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

}