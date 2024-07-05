import { CiMoneyCheck1 } from "react-icons/ci";
import "./BodyHomeAccount.css"
import React, { useEffect, useState } from 'react';

function BodyHomeAccount() {

    const [screenWidth, setScreenWidth] = useState(window.innerWidth);

    useEffect(() => {
        const handleResize = () => {
            setScreenWidth(window.innerWidth);
        };
        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);


    return (
        <div className="home_account">
            <h3>STATUS DA CONTA</h3>

            {screenWidth > 450 ? (
                <ul className="home_account_ul">
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira seu saldo total</label>
                    </li>
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira seu saldo do mês</label>
                    </li>
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira sua meta financeira</label>
                    </li>
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira sua meta mensal de investimento</label>
                    </li>
                </ul>
            ) : (
                <ul className="home_account_ul">
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira seu saldos total e do mês</label>
                    </li>
                    <li>
                        <CiMoneyCheck1 className="transactions_icone" />
                        <label>Confira suas metas finaceira e de investimento</label>
                    </li>
                </ul>
            )}

        </div>
    )
}

export default BodyHomeAccount;