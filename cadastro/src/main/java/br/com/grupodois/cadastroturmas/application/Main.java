package br.com.grupodois.cadastroturmas.application;

import br.com.grupodois.cadastroturmas.database.MongoConnection;
import br.com.grupodois.cadastroturmas.database.NeoConnection;
import br.com.grupodois.cadastroturmas.menu.MenuPessoa;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryPostgre;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryMongo;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryNeo;

import java.util.Scanner;
import com.mongodb.client.MongoDatabase;
import org.neo4j.driver.Driver;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            PessoaRepositoryPostgre pessoaRepository = new PessoaRepositoryPostgre();

            // ➕ MongoDB
            MongoDatabase database = MongoConnection.getDatabase();
            PessoaRepositoryMongo logRepository = new PessoaRepositoryMongo(database);

            // ➕ Neo4j
            Driver neoDriver = NeoConnection.getDriver();
            PessoaRepositoryNeo neoRepository = new PessoaRepositoryNeo(neoDriver);

            // ➕ Injeta os três repositórios
            MenuPessoa menuPessoa = new MenuPessoa(scanner, pessoaRepository, logRepository, neoRepository);

            int opcao;
            do {
                System.out.println("\n==== MENU PRINCIPAL ====");
                System.out.println("1. Menu de Pessoas");
                System.out.println("2. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1 -> menuPessoa.exibir();
                    case 2 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } while (opcao != 2);

            pessoaRepository.fechar();
            neoDriver.close(); // ✅ fecha conexão Neo4j corretamente
        }
    }
}
