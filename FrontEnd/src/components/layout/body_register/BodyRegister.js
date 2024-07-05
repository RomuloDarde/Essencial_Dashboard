import { Link, useNavigate } from "react-router-dom";
import { useState, useRef } from "react";
import InputMask from 'react-input-mask';
import "./BodyRegister.css"

function BodyRegister() {

  //URL Hosts
  const localhost = "http://localhost:8080/";
  const railwayhost = "https://essencial-backend.up.railway.app/";

  const navigate = useNavigate();

  //UseState - User data
  const [name, setName] = useState('');
  const [cpf, setCpf] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');


  //Button click on Key 'ENTER' press
  //*Usar essa constante na tag ref do botão
  const buttonRef = useRef(null);

  //*Usar essa constante na tag onKeyDown do Input
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      buttonRef.current.click();
    }
  };


  //Function - Post user
  const postUser = async (e) => {
    e.preventDefault();

    const response = await fetch(localhost + 'users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name,
        cpf,
        email,
        password
      })
    });

    if (!response.ok) {
      return response.json().then(errorData => {
        // Mapeia todas as mensagens de erro
        const errorMessages = errorData.fieldErrors.map(error => error.message);

        // Concatena todas as mensagens de erro em uma única string
        const concatenatedErrorMessages = errorMessages.join('\n');

        alert("Erro ao cadastrar o usuário: \n" + concatenatedErrorMessages);
        throw new Error('Erro ao cadastrar o usuário.');
      })
        .catch((error) => console.error(error))
    }

    navigate("/");
    alert("Cadastro efetuado com sucesso! Efetue o login com o seu email e senha cadastrados.");

  }


  return (
    <div className="register_body">

      <form >
        <h2>CADASTRO</h2>

        <label>Nome</label>
        <input
          type="text"
          onKeyDown={handleKeyDown}
          placeholder="Digite o seu nome"
          required
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <label>Cpf</label>
        <InputMask 
           className="inputMask" 
           onKeyDown={handleKeyDown}
           mask="999.999.999-99"
           placeholder="Digite o seu cpf"
           required
           value={cpf}
           onChange={(e) => setCpf(e.target.value)}
        />

        <label>E-mail (será o seu Login)</label>
        <input
          type="email"
          onKeyDown={handleKeyDown}
          placeholder="Digite o seu e-mail"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)} />

        <label>Senha</label>
        <input
          type="text"
          onKeyDown={handleKeyDown}
          placeholder="Digite a sua senha"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <div className="register_body_form_registerbox">
          <button
            className="register_body_form_button_register"
            ref={buttonRef}
            onClick={postUser}
          >Cadastrar</button>

          <Link to="/">
            <button className="register_body_form_button_login">Voltar para o <b>LOGIN</b></button>
          </Link>
        </div>

      </form>

      <img src="/images/logo_body.png" alt="Logomarca do Essencial." />
    </div>
  )
}

export default BodyRegister;