import "./BodyHomeWelcome.css"
import { useState, useEffect } from "react";

function BodyHomeWelcome() {

  //URL Hosts
  const localhost = "http://localhost:8080/";
  const railwayhost = "https://essencial-backend.up.railway.app/";

  //UseState
  const [userName, setUserName] = useState();
  const [welcomeMessage, setWelcomeMessage] = useState('');

  //UseState - Screen Size
  const [screenWidth, setScreenWidth] = useState(window.innerWidth);
  const [screenHeight, setScreenHeight] = useState(window.innerHeight);

  //UseEffect - User data
  useEffect(() => {
    fetch(localhost + 'users/profile', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((resp) => resp.json())
      .then((json) => setUserName(json.name))
      .catch((err) => console.log(err))
  }, [])

  //UseEffect - Array Welcome Messages
  useEffect(() => {
    const welcomeMessages = [
      "Não é sobre viver com menos, mas sim sobre viver com o suficiente, e descobrir que o suficiente é mais do que o necessário.",

      "É eliminar o desnecessário, avaliar melhor as suas compras, registrar o que entra e sai e priorizar experiências e aprendizados.",

      "Identifique o essencial. Não dê tanta importância para o resto.",

      "“Existem duas maneiras de ser rico: uma é adquirindo muito e a outra é desejando pouco.” (Jackie French Kolle)",

      "É sobre dizer “não” com mais frequência para que possa dizer “sim” às coisas que realmente importam.",

      "“As coisas mais simples costumam ser as mais verdadeiras.” (Richard Bach)",

      "Menos é mais! Mais liberdade, mais espaço, mais felicidade, mais poder, mais tempo, mais espaço, mais prazer da vida, mais autoconfiança, mais amor de si…",

      "“Você tem sucesso na vida quando tudo que realmente quer é apenas o que realmente precisa.” (Vernon Howard)",

      "“As minhas riquezas consistem, não na extensão das minhas posses, mas na escassez das minhas necessidades.” (J. Brotherton)",

      "“O segredo da felicidade, sabe, não está em buscar mais, mas em desenvolver a capacidade de desfrutar menos.” (Sócrates)",

      "Pare de comprar coisas que não precisa!"
    ];

    const randomIndex = Math.floor(Math.random() * welcomeMessages.length);
    setWelcomeMessage(welcomeMessages[randomIndex]);
  }, []);

  //Use Effect - Screen Resize
  useEffect(() => {
    const handleResize = () => {
      setScreenWidth(window.innerWidth);
      setScreenHeight(window.innerHeight);
    };
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);


  return (
    <div className="home_welcome">

      {screenWidth > 450 && screenHeight > 430 ? (
        <h3>{userName !== undefined ? `${userName.toUpperCase()}, BEM VINDO AO ESSENCIAL - FINANÇAS SIMPLIFICADAS!` : ""}</h3>
      ) : (
        <h3>{userName !== undefined ? `${userName.toUpperCase()}, BEM VINDO AO ESSENCIAL` : ""}</h3>
      )}

      <p><i>{welcomeMessage}</i></p>
    </div>
  )
}

export default BodyHomeWelcome;
