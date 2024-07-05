import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyGoals from "../layout/body_goals/BodyGoals";
import ErrorPage from "./ErrorPage";

function Goals() {
    const token = localStorage.getItem('token');  

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyGoals />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Goals;