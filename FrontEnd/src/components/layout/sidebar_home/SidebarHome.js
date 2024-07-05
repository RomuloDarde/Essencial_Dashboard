import { Link } from "react-router-dom"
import { MdNotifications, MdHelp } from "react-icons/md";
import { CiMoneyCheck1 } from "react-icons/ci";
import { TbPigMoney } from "react-icons/tb";
import { BiSolidHappyBeaming } from "react-icons/bi";
import { IoHomeOutline } from "react-icons/io5";
import { RiMoneyDollarCircleFill } from "react-icons/ri";
import "./SidebarHome.css"


function SidebarHome() {

    return (
        <div className="sidebar_home">
            <img src="/images/logo_sidebar.png" alt="Logomarca do Essencial." />

            <ul>
                <label>MENU</label>
                <Link className="sidebar_link" to="/home">
                    <li>
                        <IoHomeOutline />
                        <button>Início</button>
                    </li>
                </Link>
                <Link className="sidebar_link" to="/notifications">
                    <li>
                        <MdNotifications />
                        <button>Notificações</button>
                    </li>
                </Link>
                <Link className="sidebar_link" to="/account">
                    <li>
                        <CiMoneyCheck1 />
                        <button>Status da Conta</button>
                    </li>
                </Link>
                <Link className="sidebar_link" to="/transactions">
                    <li>
                        <RiMoneyDollarCircleFill />
                        <button>Transações</button>
                    </li>
                </Link>

                <Link className="sidebar_link" to="/goals">
                    <li>
                        <TbPigMoney />
                        <button>Metas Financeiras</button>
                    </li>
                </Link>

                <Link className="sidebar_link" to="/minimalism">
                    <li>
                        <BiSolidHappyBeaming />
                        <button>Minimalismo Financeiro</button>
                    </li>
                </Link>

                <Link className="sidebar_link" to="/help">
                    <li>
                        <MdHelp />
                        <button>Como usar o Essencial?</button>
                    </li>
                </Link>
            </ul>

        </div >
    )
}

export default SidebarHome;
