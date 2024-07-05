import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyMinimalism from "../layout/body_minimalism/BodyMinimalism";
import ErrorPage from "./ErrorPage";

 function Minimalism() {
    const token = localStorage.getItem('token');

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyMinimalism />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
 }
 export default Minimalism;