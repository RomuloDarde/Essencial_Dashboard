import { Link } from "react-router-dom"

function ErrorPage() {
    return (
        <div>
            <h3
                style={{ textAlign: "center", margin: "3rem" }}>
                Ops! Parece que você tentou acessar uma página sem fazer login.
                <br /> <br />Para garantir a segurança dos seus dados, é necessário realizar o login antes de acessar esta área.
                <br /> <br />Por favor, faça o login para continuar.
            </h3>

            <Link to="/">
                <h3
                    style={{ textAlign: "center", margin: "3rem", cursor: "pointer" }}>
                    Voltar para a página de Login.
                </h3>
            </Link>
        </div>

    )
}

export default ErrorPage;