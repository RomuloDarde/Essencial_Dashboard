# Usar uma imagem base do Node.js
FROM node:18-alpine

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo de dependências
COPY package.json package-lock.json ./

# Instalar as dependências do projeto e limpar o cache do npm para reduzir o tamanho da imagem final
RUN npm install && npm cache clean --force

# Copiar os demais arquivos do projeto para o contêiner, incluindo o código fonte da aplicação
COPY . .

# Compilar a aplicação React - A saída normalmente é colocada no diretório build.
RUN npm run build

# Instalar o servidor HTTP simples para servir aplicações estáticas
RUN npm install -g serve

# Expôr a porta que o servidor HTTP irá utilizar 
EXPOSE 3000

# Definir o comando de inicialização do contêiner - serve o conteúdo estático do diretório build na porta 3000
CMD ["serve", "-s", "build"]
