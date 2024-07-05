import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyAccount from "../layout/body_account/BodyAccount";
import ErrorPage from "./ErrorPage";

function Account() {
    const token = localStorage.getItem('token'); 

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyAccount />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Account;