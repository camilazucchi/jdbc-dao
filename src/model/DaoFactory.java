package model;

import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;

/* A classe "DaoFactory" é um padrão de design que fornece uma maneira de obter instâncias de classes DAO sem expor
 * a lógica de criação dessas instâncias diretamente no código cliente. O objetivo principal da "DaoFactory" é
 * encapsular a criação de objetos DAO, promovendo a flexibilidade, manutenibilidade e teste da aplicação. */
public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }

}
