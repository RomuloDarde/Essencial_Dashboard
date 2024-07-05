import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyHome from "../layout/body_home/BodyHome";
import ErrorPage from "./ErrorPage";

function Home() {
    const token = localStorage.getItem('token');

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyHome />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Home;