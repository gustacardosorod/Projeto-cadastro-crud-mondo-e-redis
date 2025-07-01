package br.com.grupodois.cadastroturmas.repository;

import br.com.grupodois.cadastroturmas.util.LocalDateAdapter;
import br.com.grupodois.cadastroturmas.database.PostgreConnection;
import br.com.grupodois.cadastroturmas.database.RedisCache;
import br.com.grupodois.cadastroturmas.model.Pessoa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepositoryPostgre {
    private final RedisCache redisCache;
    private final Gson gson;
    private final String CACHE_KEY = "pessoas:listar";

    public PessoaRepositoryPostgre() {
        this.redisCache = new RedisCache();
        this.gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();  // ⬅️ agora Gson entende LocalDate
                    
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL não encontrado.");
        }
    }

    public void adicionar(Pessoa pessoa) {
        long start = System.currentTimeMillis();

        String sql = "INSERT INTO pessoas (id, nome, email, cpf, data_nascimento, trabalho) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = PostgreConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pessoa.getId());
            stmt.setString(2, pessoa.getNome());
            stmt.setString(3, pessoa.getEmail());
            stmt.setString(4, pessoa.getCpf());
            stmt.setDate(5, Date.valueOf(pessoa.getDataNascimento()));
            stmt.setString(6, pessoa.getTrabalho());

            stmt.executeUpdate();
            redisCache.del(CACHE_KEY);  // Invalida cache
            System.out.println("Pessoa cadastrada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir pessoa no PostgreSQL:");
            e.printStackTrace();
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Tempo de execução (ADICIONAR): " + duration + " ms");
    }

    public void listar() {
        long start = System.currentTimeMillis();

        String jsonCache = redisCache.get(CACHE_KEY);

        if (jsonCache != null) {
            Pessoa[] pessoas = gson.fromJson(jsonCache, Pessoa[].class);
            for (Pessoa p : pessoas) {
                exibirPessoa(p);
            }
            System.out.println("Tempo de execução (LISTAR - CACHE): " + (System.currentTimeMillis() - start) + " ms");

        } else {
            List<Pessoa> lista = new ArrayList<>();
            String sql = "SELECT * FROM pessoas";

            try (Connection conn = PostgreConnection.getConnection();

                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Pessoa pessoa = new Pessoa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("cpf"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("trabalho")
                    );
                    lista.add(pessoa);
                }

                String json = gson.toJson(lista);
                redisCache.set(CACHE_KEY, json); // Salva no cache

                for (Pessoa p : lista) {
                    exibirPessoa(p);
                }

                System.out.println("Tempo de execução (LISTAR - POSTGRES): " + (System.currentTimeMillis() - start) + " ms");

            } catch (SQLException e) {
                System.err.println("Erro ao listar pessoas:");
                e.printStackTrace();
            }
        }
    }

    public void atualizar(int id, String novoNome) {
        long start = System.currentTimeMillis();

        String sql = "UPDATE pessoas SET nome = ? WHERE id = ?";

        try (Connection conn = PostgreConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setInt(2, id);

            stmt.executeUpdate();
            redisCache.del(CACHE_KEY);  // Invalida cache
            System.out.println("Pessoa atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pessoa:");
            e.printStackTrace();
        }

        System.out.println("Tempo de execução (ATUALIZAR): " + (System.currentTimeMillis() - start) + " ms");
    }

    public void remover(int id) {
        long start = System.currentTimeMillis();

        String sql = "DELETE FROM pessoas WHERE id = ?";

        try (Connection conn = PostgreConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            redisCache.del(CACHE_KEY);  // Invalida cache
            System.out.println("Pessoa removida com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao remover pessoa:");
            e.printStackTrace();
        }

        System.out.println("Tempo de execução (REMOVER): " + (System.currentTimeMillis() - start) + " ms");
    }

    public void fechar() {
        redisCache.close();
    }

    private void exibirPessoa(Pessoa p) {
        System.out.println("ID: " + p.getId());
        System.out.println("Nome: " + p.getNome());
        System.out.println("Email: " + p.getEmail());
        System.out.println("CPF: " + p.getCpf());
        System.out.println("Data de Nascimento: " + p.getDataNascimento());
        System.out.println("Trabalho: " + p.getTrabalho());
        System.out.println("-----------------------");
    }
}
