package model.dao.impl;
/* O package impl conterá implementações do DAO */

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO seller " +
                "(name, email, birthdate, basesalary, departmentid) " +
                "VALUES " +
                "(?, ?, ?, ?, ?) ",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            /* Esta linha verifica se alguma linha foi afetada pela operação de banco de dados. Normalmente, isso
            * significa que a operação foi bem-sucedida e pelo menos uma linha foi inserida. */
            if (rowsAffected > 0) {
                /* Aqui estamos obtendo um "ResultSet" contendo as chaves geradas pela operação de inserção. O método
                * "getGeneratedKeys()" geralmente é chamado após uma operação de inserção bem-sucedida para obter as
                * chaves primárias geradas automaticamente pelo banco de dados. */
                ResultSet rs = st.getGeneratedKeys();
                /* Esta linha verifica se há alguma linha no "ResultSet". Se houver, significa que pelo menos uma
                * chave foi inserida: */
                if (rs.next()) {
                    /* Aqui, estamos obtendo o valor da primeira coluna do "ResultSet", que normalmente contém a
                    * chave primária gerada. Este valor é armazenado na variável "id": */
                    int id = rs.getInt(1);
                    /* Define o valor de ID no objeto "Seller" através do método "setId()": */
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
                /* Lança uma exception caso nenhuma linha seja afetada: */
            } else {
                throw new DbException("Unexpected error! No rows were affected.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    // Método simples que realiza a atualização de um vendedor no banco de dados:
    @Override
    public void update(Seller obj) {
        try (PreparedStatement st = conn.prepareStatement("UPDATE seller " +
                "SET name = ?, email = ?, birthdate = ?, basesalary = ?, departmentid = ? " +
                "WHERE id = ?")) {
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM seller " +
                "WHERE id = ?")) {
            // O valor será o ID que foi informado como parâmetro "(Integer id)":
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();

            // Verifica se algum registro foi afetado pela exclusão:
            if (rowsAffected == 0) {
                // Lança uma exceção informando que o vendedor com o ID fornecido não foi encontrado:
                throw new DbException("Seller with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
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

    // Este método retorna uma lista de todos os vendedores de todos os departamentos:
    @Override
    public List<Seller> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "ORDER BY Name")) {
            return executeQueryAndProcessResultMultiple(st);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
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