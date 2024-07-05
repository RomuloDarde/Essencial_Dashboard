import { Link, useNavigate } from "react-router-dom"
import { MdOutlineEmail, MdLockOutline } from "react-icons/md";
import { useState, useRef } from "react";

import "./BodyLogin.css"

function BodyLogin() {

  //URL Hosts
  const localhost = "http://localhost:8080/";
  const railwayhost = "https://essencial-backend.up.railway.app/";

  const navigate = useNavigate();

  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');


  //Function - Button click on Key 'ENTER' press
  //*Usar essa constante na tag ref do botão
  const buttonRef = useRef(null);

  //*Usar essa constante na tag onKeyDown do Input
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      buttonRef.current.click();
    }
  };


  // Function - Handle submit
  const handleSubmit = async (e) => { // define a função handleSubmit como assíncrona
    e.preventDefault();

    try {
      const response = await fetch(localhost + 'users/login', { // espera pela resposta da requisição fetch
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          login,
          password
        })
      });

      if (response.status === 404) {
        alert('Erro ao fazer login, o e-mail está incorreto.');
        throw new Error('Erro ao fazerc login, o e-mail está incorreto.');

      } else if (response.status === 403) {
        alert('Erro ao fazer login, a senha está incorreta.');
        throw new Error('Erro ao fazer login, a senha está incorreta.');
      }


      const data = await response.json(); // espera pelo corpo da resposta sendo convertido em JSON
      const token = data.token;

      localStorage.setItem('token', token);
      console.log(token);

      // Redirecionar ou executar outras ações após o login
      navigate("/home");

    } catch (error) {
      console.error('Erro ao fazer login:', error);
    }
  };

  const forgotPassword = () => {
    alert("Favor entrar em contato com este endereço de email (deve ser através de seu email cadastrado) e solicitar uma nova senha: romulo.darde@gmail.com");
  }


  return (
    <div className="login_body">
      <img src="/images/logo_body.png" alt="Logomarca do Essencial." />

      <form>
        <h2>LOGIN</h2>

        <div className="login_body_form_inputbox">
          <input
            type="text"
            onKeyDown={handleKeyDown}
            placeholder="Digite o seu e-mail"
            required
            value={login}
            onChange={(e) => setLogin(e.target.value)}
          />

          <MdOutlineEmail />
        </div>

        <div className="login_body_form_inputbox">
          <input
            type="password"
            onKeyDown={handleKeyDown}
            placeholder="Digite a sua senha"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <MdLockOutline />
        </div>

        <button
          className="login_body_form_button_forgotpassword"
          onClick={forgotPassword}
        >Esqueceu sua senha?</button>

        <button
          className="login_body_form_button"
          ref={buttonRef}
          type="submit"
          onClick={handleSubmit}
        >Acessar a conta →
        </button>

        <div className="login_body_form_registerbox">
          <label>Ainda não é cadastrado?</label>
          <Link to="/register">
            <button><b>CADASTRE-SE AGORA</b></button>
          </Link>
        </div>

      </form>
    </div>
  )
}

export default BodyLogin;
