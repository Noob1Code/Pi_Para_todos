
# Configuração do Projeto GreenLog

Este guia descreve os passos necessários para configurar o ambiente de desenvolvimento tanto para o frontend quanto para o backend do projeto **GreenLog**.

---

## 📦 Frontend (`greenlog-front arrumado`)

1. Abra a pasta `greenlog-front arrumado` no Visual Studio Code.
2. No terminal da própria pasta, execute o comando abaixo para instalar todas as dependências necessárias:
   ```bash
   npm install
   ```

---

## 🛠️ Backend (`greenlog-back`)

1. Abra a pasta `greenlog-back` no seu ambiente de desenvolvimento.
2. Execute as ações:
   - **Clean and Build**
   - **Build with Dependencies**
3. Localize e abra o arquivo `application.properties`.
4. Altere os seguintes campos com os dados do seu banco de dados:
   ```properties
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```
5. Substitua o nome do banco na URL da conexão:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/NOME_DO_SEU_BANCO?currentSchema=public
