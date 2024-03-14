package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/* Nesta classe ficarão os métodos estáticos auxiliares, que, basicamente, servem para obter e fechar uma conexão com o
 * banco de dados. */
public class DB {
    private static Connection connection = null;

    private DB() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /* Este método garante que apenas uma única instância de conexão seja criada e reutilizada sempre que necessário,
     * melhorando a eficiência e a gestão de recursos na aplicação. */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return connection;
    }

    /* Este método é responsável por fechar a conexão com o banco de dados, liberando os recursos associados a ela. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    /* Este método carrega as configurações do banco de dados a partir de um arquivo "db.properties" e retorna essas
     * configurações como um objeto "Properties". Isso é útil para configurar a conexão com o banco de dados de forma
     * flexível e externa ao código-fonte. */
    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    /* Este método também é uma função utilitária igual ao método "closeStatement", mas este serve para fechar um
    * objeto "ResultSet". */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    /* Este método é uma função utilitária para fechar um objeto "Statement". Ele faz uma verificação de nulidade para
    * garantir que o objeto "Statement" não seja nulo antes de tentar fechar. Se o "statement" não for nulo, o bloco
    * de código dentro do "if" será executado. */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}