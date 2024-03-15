package model.dao.impl;
/* O package impl conterá implementações do DAO */

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/* Esta classe implementa a interface "SellerDao" e consequentemente todos os seus métodos */
public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

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
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            /* Define uma String contendo a consulta SQL que seleciona todos os campos da tabela "seller" e o campo
            * "Name" da tabela "department", renomeando-o para "DepName": */
            String sql = "SELECT seller.*, department.Name as DepName " +
                    "FROM seller " +
                    "INNER JOIN department ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?";
            // Prepara a instrução SQL para ser executada pelo banco de dados:
            st = conn.prepareStatement(sql);
            // Define o valor do parâmetro na posição "1" (o "?" na consulta) como o valor do "id" fornecido ao método.
            st.setInt(1, id);
            // Executa a consulta preparada e armazena o resultado em um objeto "ResultSet":
            rs = st.executeQuery();

            // Verifica se há pelo menos uma linha no resultado da consulta:
            if (rs.next()) {
                // Cria uma nova instância de "Department":
                Department department = new Department();
                // Define o ID do departamento usando o valor da coluna "departmentid" no resultado da consulta:
                department.setId(rs.getInt("departmentid"));
                // Define o nome do departamento usando o valor da coluna "depname" no resultado da consulta:
                department.setName(rs.getString("depname"));
                // Cria uma nova instância de "Seller":
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
                obj.setDepartment(department);
                // Retorna o objeto "Seller" preenchido com as informações recuperadas do banco de dados:
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

}