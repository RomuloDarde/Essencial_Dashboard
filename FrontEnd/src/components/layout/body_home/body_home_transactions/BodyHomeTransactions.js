import { RiMoneyDollarCircleFill } from "react-icons/ri";
import React, { useEffect, useState } from 'react';
import "./BodyHomeTransactions.css"

function BodyHomeTransactions() {
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
        <div className="home_transactions">
            <h3>TRANSAÇÕES</h3>

            {screenWidth > 450 ? (
                <ul className="home_transactions_ul">
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Cadastre suas transações</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Consulte suas transações do mês</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Consulte suas transações de meses anteriores</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Consulte suas transações por período</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Consulte suas despesas por categoria</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Consulte um ranking das despesas por categoria</label>
                    </li>
                </ul>

             ) : (
                <ul className="home_transactions_ul">
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Cadastre suas transações</label>
                    </li>
                    <li>
                        <RiMoneyDollarCircleFill className="transactions_icone" />
                        <label>Realize consultas personalizadas</label>
                    </li>
                </ul>
            )}

        </div>
    )
}

export default BodyHomeTransactions;
