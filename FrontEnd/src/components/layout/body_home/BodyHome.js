import { Link } from "react-router-dom"
import "./BodyHome.css"
import BodyHomeWelcome from "./body_home_welcome/BodyHomeWelcome";
import BodyHomeTransactions from "./body_home_transactions/BodyHomeTransactions";
import BodyHomeAccount from "./body_home_account/BodyHomeAccount";

function BodyHome() {
    return (
        <div className="home">
            <BodyHomeWelcome />

            <div className="home_ta">
                <Link className="home_link" to="/transactions">
                    <BodyHomeTransactions />
                </Link>

                <Link className="home_link" to="/account">
                    <BodyHomeAccount />
                </Link>
            </div>
        </div>

    )
}
export default BodyHome;    