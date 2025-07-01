package br.com.grupodois.cadastroturmas.menu;

import br.com.grupodois.cadastroturmas.model.Pessoa;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryPostgre;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryMongo;
import br.com.grupodois.cadastroturmas.repository.PessoaRepositoryNeo;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuPessoa {
    private final Scanner scanner;
    private final PessoaRepositoryPostgre repository;
    private final PessoaRepositoryMongo logRepository;
    private final PessoaRepositoryNeo neoRepository; // ➕ repositório Neo4j

    public MenuPessoa(Scanner scanner, PessoaRepositoryPostgre repository, PessoaRepositoryMongo logRepository, PessoaRepositoryNeo neoRepository) {
        this.scanner = scanner;
        this.repository = repository;
        this.logRepository = logRepository;
        this.neoRepository = neoRepository;
    }

    public void exibir() {
        int opcao;
        do {
            System.out.println("\n==== MENU PESSOA ====");
            System.out.println("1. Adicionar Pessoa");
            System.out.println("2. Listar CPF");
            System.out.println("3. Atualizar Pessoa");
            System.out.println("4. Remover Pessoa");
            System.out.println("5. Ver Conexões Profissionais");
            System.out.println("6. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpa o buffer

            switch (opcao) {
                case 1:
                    System.out.print("ID da pessoa: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nome da pessoa: ");
                    String nome = scanner.nextLine();

                    System.out.print("Email da pessoa: ");
                    String email = scanner.nextLine();

                    System.out.print("CPF da pessoa: ");
                    String cpf = scanner.nextLine();

                    System.out.print("Data de nascimento (AAAA-MM-DD): ");
                    String dataStr = scanner.nextLine();
                    LocalDate dataNascimento = LocalDate.parse(dataStr);

                    System.out.print("Trabalho da pessoa: ");
                    String trabalho = scanner.nextLine();

                    Pessoa novaPessoa = new Pessoa(id, nome, email, cpf, dataNascimento, trabalho);
                    repository.adicionar(novaPessoa);
                    neoRepository.adicionarPessoa(novaPessoa); // ➕ adiciona ao grafo
                    logRepository.registrarLog("ADICIONAR", "Pessoa adicionada: " + nome + " (ID: " + id + ")");
                    break;

                case 2:
                    repository.listar();
                    logRepository.registrarLog("LISTAR", "Listagem de pessoas realizada.");
                    break;

                case 3:
                    System.out.print("ID da pessoa a atualizar: ");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();

                    repository.atualizar(idAtualizar, novoNome);
                    logRepository.registrarLog("ATUALIZAR", "Pessoa ID " + idAtualizar + " atualizada para nome: " + novoNome);
                    break;

                case 4:
                    System.out.print("ID da pessoa a remover: ");
                    int idRemover = scanner.nextInt();
                    scanner.nextLine();

                    repository.remover(idRemover);
                    logRepository.registrarLog("REMOVER", "Pessoa ID " + idRemover + " removida.");
                    break;

                case 5:
                    System.out.print("ID da pessoa para ver conexões: ");
                    int idConsulta = scanner.nextInt();
                    scanner.nextLine();
                    neoRepository.listarConexoesProfissionais(idConsulta); // ➕ lista conexões no grafo
                    break;

                case 6:
                    System.out.println("Voltando ao menu principal...");
                    logRepository.registrarLog("MENU", "Usuário saiu do MenuPessoa.");
                    break;

                default:
                    System.out.println("Opção inválida!");
                    logRepository.registrarLog("ERRO", "Opção inválida escolhida no menu: " + opcao);
            }
        } while (opcao != 6);
    }
}
