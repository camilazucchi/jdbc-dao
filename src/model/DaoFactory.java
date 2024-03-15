package model;

import db.DB;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;

/* A classe "DaoFactory" é um padrão de design que fornece uma maneira de obter instâncias de classes DAO sem expor
 * a lógica de criação dessas instâncias diretamente no código cliente. O objetivo principal da "DaoFactory" é
 * encapsular a criação de objetos DAO, promovendo a flexibilidade, manutenibilidade e teste da aplicação. */
public class DaoFactory {
    /* SonarLint: Add a private constructor to hide the implicit public one.
    * O que isso significa?
    * Quando uma classe não declara explicitamente um construtor, o Java fornece um construtor público padrão
    * implícito. Isso significa que qualquer código externo pode criar instâncias dessa classe usando esse construtor
    * padrão.
    * Se desejamos controlar como as instâncias da classe são criadas e limitar o acesso a elas, é uma boa prática
    * declarar um construtor privado e fornecer métodos estáticos ou de fábrica para criar instâncias. Isso é conhecido
    * como padrão de projeto Singleton. */
    private DaoFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated ");
    }

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

}
