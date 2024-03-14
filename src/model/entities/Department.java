package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {
    /* Quando uma classe em Java implementa a interface "Serializable", isso significa que os objetos dessa classe
    * podem ser serializados. Em termos simples, a serialização é o processo de converter o estado de um objeto em
    * uma sequência de bytes, geralmente para armazenamento persistente (por exemplo, em arquivos) ou para transmissão
    * através de uma rede.
    * A única finalidade da interface "Serializable" é uma interface de marcação, o que significa que não contém
    * métodos a serem implementados. Sua única finalidade é indicar ao compilador que a classe é serializável e,
    * portanto, pode ser processada pela API de serialização do Java. */

    private Integer id;
    private String name;

    public Department() {}

    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Os métodos "equals" e "hashCode" são comumente implementados em classes Java para permitir a comparação e o
    * uso correto dessas classes em coleções, como "HashMap", "HashSet", entre outras. Eles são usados principalmente
    * para garantir que objetos sejam corretamente comparados e armazenados em coleções baseadas em hash. */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Department that = (Department) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}