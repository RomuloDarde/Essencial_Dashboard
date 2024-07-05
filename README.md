<h1>Essencial - Finanças Simplificadas</h1>

<h3>Introdução</h3>

**Essencial - Finanças Simplificadas** é um aplicativo de DashBoard Financeiro desenvolvido com SpringBoot no backend e React no frontend, baseado no tema de **Minimalismo Financeiro**. 
<br />Nosso objetivo é ajudar os usuários a gerenciarem suas finanças de forma simples e eficaz, focando no essencial.
<br />
<br />

<h3>Funcionalidades</h3>

- **Finanças registradas em um só lugar:** Com o Essencial, o usuário pode adicionar as transações de todas as suas contas bancárias e cartões de crédito em um só local.
Isso o permitirá uma visão mais abrangente e consolidada de suas finanças.

- **Registrar as transações:** O usuário pode acompanhar suas despesas e receitas diárias, além de categorizar cada transação para uma análise detalhada de seus hábitos financeiros.

- **Definir Metas Financeiras:** O usuário pode estabelecer Metas Financeiras claras, como economizar para uma viagem, adquirir algo muito desejado ou até quitar dívidas.
O Essencial o ajudará a monitorar o seu progresso e manter o foco em seus objetivos.

- **Guardar dinheiro todo o Mês:** O usuário pode criar uma Meta de Investimento, que seria uma "reserva" de o quanto ele quer guardar a cada mês para economizar e aplicar em algum Fundo de Investimento.

- **Analisar os gastos:** O usuário pode utilizar nossos gráficos e relatórios para identificar padrões de gastos e encontrar oportunidades de economia. 
Apresentamos uma série de consultas, incluindo gráficos, permitindo uma análise de quais categorias representam as maiores despesas, o que o facilita a identificar onde se pode economizar mais.
<br />


<h3>Tecnologias Utilizadas</h3>

Backend: **SpringBoot**
<br />Frontend: **React**
<br />Banco de Dados: **MySQL**
<br />Autenticação: Através do **Token JWT**
<br />
<br />

<h3>Instalação</h3>
Pré-requisitos:

- **Java**
- **Node.js e npm**
- **MySQL**
<br />


<h3>Passos para a instalação</h3>

**Clone o repositório:**
<br />git clone https://git.zallpylabs.com/go-tech/essencial_dashboard_financeiro.git
<br />

**Navegue para o diretório do backend e instale as dependências:**
<br />cd essencial_dashboard_financeiro/BackEnd
<br />./mvnw clean install
<br />

**Configure o banco de dados no application.properties:**
<br />spring.datasource.url=jdbc:mysql://localhost/financial_dashboard
<br />spring.datasource.username=root
<br />spring.datasource.password=${DB_PASSWORD}
<br />

**Inicie o backend:**
<br />Navegue para o diretório target: cd target
<br />java -jar financial_dashboard-3.2.3.jar
<br />


**Navegue para o diretório do frontend e instale as dependências:**
<br />cd ../Front\ End/
<br />npm install
<br />

**Inicie o frontend:**
<br />npm start
<br />
<br />

<h3>Uso</h3>
Acesse o aplicativo em http://localhost:3000.
<br />Crie uma conta e faça login.
<br />Navegue pelo Menu e páginas do aplicativo para descubrir as suas funcionalidades.
<br />
<br />


<h3>Contato</h3>

Para mais informações, entre em contato pelo email: romulo.darde@gmail.com - Rômulo Schmidt
