import "./BodyUpdateUser.css"
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"

function BodyUpdateUser() {

    //URL Hosts
    const localhost = "http://localhost:8080/";
    const railwayhost = "https://essencial-backend.up.railway.app/";

    const navigate = useNavigate();

    //UseState - GET
    const [userData, setUserData] = useState();

    //UseState - Screen Size
    const [screenWidth, setScreenWidth] = useState(window.innerWidth);
    const [screenHeight, setScreenHeight] = useState(window.innerHeight);

    //UseState - PUT
    const [name, setName] = useState();
    const [cpf, setCpf] = useState();
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();


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
            .then((json) => setUserData(json))
            .catch((err) => console.log(err))
    }, [])


    //Function - Update user
    const updateUser = async (e) => {
        e.preventDefault();

        if (email !== undefined) {
            var confirm = window.confirm(
                "Você será direcionado à página de login para acesso já com o novo email. Confirma a alteração do e-mail?");
            if (!confirm) {
                return;
            }
        }

        const response = await fetch(localhost + 'users/profile', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
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

                console.log(response);

                alert("Erro ao atualizar os dados do usuário: \n" + concatenatedErrorMessages);
                throw new Error('Erro ao atualizar os dados do usuário.');
            })
                .catch((error) => console.error(error))
        }

        try {
            if (name === undefined && cpf === undefined && email === undefined && password === undefined) {
                alert("Todos os campos estão vazios.");
                throw new Error("Todos os campos estão vazios.");

            } else if (email !== undefined) {
                localStorage.setItem("token", null);
                navigate("/");

            } else {
                window.location.reload();
            }
        } catch (error) {
            console.error(error);
        }

    }

    return (
        <div className="body_updateuser">
            <div className="updateuser_data_update_logo">
                <div className="updateuser_dataandupdate">

                    {screenHeight > 660 &&
                        <div className="user_data">
                            <h3>DADOS DO USUÁRIO</h3>
                            <form>
                                <div className="user_data_field">
                                    <p>Nome:</p>
                                    <h4>{userData !== undefined ? userData.name : ""}</h4>
                                </div>
                                <div className="user_data_field">
                                    <p>Cpf:</p>
                                    <h4>{userData !== undefined ? userData.cpf : ""}</h4>
                                </div>
                                <div className="user_data_field">
                                    <p>E-mail:</p>
                                    <h4>{userData !== undefined ? userData.email : ""}</h4>
                                </div>
                                <div className="user_data_field">
                                    <p>Cadastro:</p>
                                    <h4>{userData !== undefined ? userData.registrationDate : ""}</h4>
                                </div>
                            </form>
                        </div>
                    }

                    <div className="user_update">
                        <h3>ATUALIZAR DADOS</h3>
                        <form >
                            <div className="updateuser_inputbox">
                                <label>Nome:</label>
                                <input
                                    type="text"
                                    placeholder={userData !== undefined && screenHeight < 660 ? userData.name : "Digite o seu nome"}
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    onFocus={(e) => { if (e.target.value === "") setName(undefined); }}
                                    onBlur={(e) => { if (e.target.value === "") setName(undefined); }}
                                />
                            </div>

                            <div className="updateuser_inputbox">
                                <label>Cpf:</label>
                                <input
                                    type="text"
                                    placeholder={userData !== undefined && screenHeight < 660 ? userData.cpf : "Digite o seu cpf"}
                                    value={cpf}
                                    onChange={(e) => setCpf(e.target.value)}
                                    onFocus={(e) => { if (e.target.value === "") setCpf(undefined); }}
                                    onBlur={(e) => { if (e.target.value === "") setCpf(undefined); }}
                                />
                            </div>

                            <div className="updateuser_inputbox">
                                <label>{screenWidth > 650 ? "E-mail (Login):" : "Email:"}</label>
                                <input
                                    type="email"
                                    placeholder={userData !== undefined && screenHeight < 660 ? userData.email : "Digite o seu e-mail"}
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    onFocus={(e) => { if (e.target.value === "") setEmail(undefined); }}
                                    onBlur={(e) => { if (e.target.value === "") setEmail(undefined); }}
                                />
                            </div>

                            <div className="updateuser_inputbox">
                                <label>Senha:</label>
                                <input
                                    type="text"
                                    placeholder="Digite a sua senha"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    onFocus={(e) => { if (e.target.value === "") setPassword(undefined); }}
                                    onBlur={(e) => { if (e.target.value === "") setPassword(undefined); }}
                                />
                            </div>
                        </form >

                        <button
                            className="update_button"
                            onClick={updateUser}
                        >Atualizar</button>
                    </div>
                </div>

                <img src="/images/logo_body.png" alt="Logomarca do Essencial." />
            </div>

        </div >
    )
}
export default BodyUpdateUser;