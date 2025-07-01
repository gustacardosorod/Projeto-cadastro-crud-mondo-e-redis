📚 Sistema de Cadastro com MongoDB, Java e Redis
🎯 Objetivo
Este projeto tem como objetivo principal explorar o funcionamento de um banco de dados NoSQL, integrando o MongoDB com Java e utilizando o Redis como sistema de cache em memória para otimizar consultas e operações de CRUD.

📝 Descrição
Gustavo Cardoso desenvolveu um sistema de cadastro simples em Java que realiza operações completas de CRUD em um banco de dados MongoDB. Executado localmente através do VS Code, o sistema não possui interface gráfica nem API, focando na interação direta com o banco de dados.

A integração com o Redis permite que consultas sejam cacheadas em memória após a primeira execução, melhorando significativamente o desempenho ao evitar acessos repetitivos ao MongoDB.


⚙️ Funcionalidades
Inserção, atualização e exclusão de documentos em diferentes coleções do MongoDB.

Listagem de documentos armazenados com suporte a filtros complexos.

Utilização do MongoDB Driver Sync para operações síncronas eficientes.

Implementação de cache com Redis para otimização de consultas subsequentes.

🧪 Tecnologias Utilizadas
Java 21

MongoDB

MongoDB Driver Sync (4.9.0)

Maven

Redis

Jedis (Redis Java client)

🚀 Como Executar
Certifique-se de ter o MongoDB e o Redis instalados e em execução localmente:

MongoDB: mongodb://localhost:27017

Redis: redis://localhost:6379

Clone o repositório:

bash
Copiar
Editar
git clone https://github.com/wisidev/CadastroTurmas.git
cd cadastro
Abra o projeto na sua IDE (VS Code).

Execute a classe Main.java.

🎥 Demonstração em Vídeo
Um vídeo demonstrando o funcionamento do sistema está disponível no YouTube: 



