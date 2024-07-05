import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyHelp from "../layout/body_help/BodyHelp"
import ErrorPage from "./ErrorPage";

function Help() {
    const token = localStorage.getItem('token'); 

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyHelp />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Help;