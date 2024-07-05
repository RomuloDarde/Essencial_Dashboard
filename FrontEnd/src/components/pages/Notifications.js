import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyNotifications from "../layout/body_notifications/BodyNotifications";
import ErrorPage from "./ErrorPage";

function Notifications() {
    const token = localStorage.getItem('token');

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyNotifications/>
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Notifications;