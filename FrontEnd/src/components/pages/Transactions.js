import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import BodyTransactions from "../layout/body_transactions/BodyTransactions";
import ErrorPage from "./ErrorPage";

function Transactions() {
    const token = localStorage.getItem('token');

    return (
        <div>
            {token.length > 20 &&
                <>
                    <SidebarHome />
                    <BodyTransactions />
                    <HeaderHome />
                </>
            }
            {token.length < 20 && 
                <ErrorPage />
            }
        </div>
    )
}
export default Transactions;