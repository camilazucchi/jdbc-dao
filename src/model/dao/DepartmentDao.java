package model.dao;

import model.entities.Department;

import java.util.List;

/* Esta interface define um contrato para operações de acesso a dados relacionadas ao objeto "Department".
* Os métodos abaixo são responsáveis por:
* - Inserir um novo departamento no sistema
* - Atualizar um departamento existente no sistema
* - Excluir um departamento do sistema com base em seu ID
* - Encontrar e retornar um departamento com base em seu ID
* - Encontrar e retornar todos os departamentos existentes no sistema retornando uma "List" de objetos "Department" */
public interface DepartmentDao {

    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();

}