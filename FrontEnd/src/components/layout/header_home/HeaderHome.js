import { Link, useNavigate } from "react-router-dom"
import "./HeaderHome.css"
import { useState } from "react";
import { IoLogOutSharp } from "react-icons/io5";
import { FaUserCircle, FaUser } from "react-icons/fa";
import SearchBar from "../../items/header_home/SearchBar";

function HeaderHome() {
    const navigate = useNavigate();

    const [showUserFields, setShowUserFields] = useState(false);

    function userboxClick() {
        setShowUserFields(!showUserFields);
    };

    const logOut = () => {
        localStorage.setItem("token", null);
        console.log(localStorage.getItem("token"));
        navigate("/");
    }

    return (
        <div className="header_home">
            <div className="header_body">
                <SearchBar />

                <div className="header_body_userbox" onClick={userboxClick}>
                    <button>Usuário</button>
                    <FaUserCircle />
                </div>
            </div>

            {showUserFields &&
                <ul>
                    <Link className="userfields_link" to="/profile">
                        <li>
                            <FaUser />
                            <button>Dados Cadastrais</button>
                        </li>
                    </Link>

                    <li onClick={() => logOut()}>
                        <IoLogOutSharp />
                        <button>Sair</button>
                    </li>
                </ul>
            }
        </div>
    )
}

export default HeaderHome;
