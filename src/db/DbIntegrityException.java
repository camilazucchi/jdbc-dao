package db;

/* O erro de integridade referencial ocorre quando há uma violação das regras de integridade referencial em um
 * banco de dados relacional. Essas regras são projetadas para garantir que as relações entre as tabelas do banco de
 * dados permaneçam consistentes e válidas. Quando um banco de dados é projetado com chaves estrangeiras que refereniam
 * chaves primárias em outras tabelas, a integridade referencial garante que as relações entre essas tabelas sejam
 * mantidas. */
public class DbIntegrityException extends RuntimeException {
    public DbIntegrityException(String message) {
        super(message);
    }
}