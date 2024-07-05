import "./BodyNotifications.css"
import { useState, useEffect } from "react";

function BodyNotifications() {

    //URL Hosts
    const localhost = "http://localhost:8080/";
    const railwayhost = "https://essencial-backend.up.railway.app/";

    //UseState
    const [accountNotification, setAccountNotification] = useState();
    const [financialGoalNotification, setFinancialGoalNotification] = useState();
    const [investmentGoalNotification, setInvestmentGoalNotification] = useState();


    //UseEffect
    //AccountNotification
    useEffect(() => {
        fetch(localhost + 'accounts/notification', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => resp.text())
            .then((notification) => setAccountNotification(notification))
            .catch((err) => console.log(err))
    }, [])


    //FinancialGoalNotification
    useEffect(() => {
        fetch(localhost + 'financialgoals/notification', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => {
                if (!resp.ok) {
                    throw new Error(resp.status);
                }
                return resp.text();
            })
            .then((notification) => setFinancialGoalNotification(notification))
            .catch((err) => {
                if (err.message === '404') {
                    setFinancialGoalNotification("Não há nenhuma Meta Financeira cadastrada.");
                } else {
                    setFinancialGoalNotification("Erro: " + err.message)
                }
            })
    }, [])


    //InvestmentGoalNotification
    useEffect(() => {
        fetch(localhost + 'investmentgoals/notification', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((resp) => {
                if (!resp.ok) {
                    throw new Error(resp.status);
                }
                return resp.text();
            })
            .then((notification) => setInvestmentGoalNotification(notification))
            .catch((err) => {
                if (err.message === '404') {
                    setInvestmentGoalNotification("Não há nenhuma Meta de Investimento Mensal cadastrada.");
                } else {
                    setInvestmentGoalNotification("Erro: " + err.message)
                }
            });
    }, [])


    return (
        <div className="body_notifications">
            <form >
                <h2>NOTIFICAÇÕES</h2>

                <div>
                    <label><b>Conta</b></label>
                    <textarea type="text" placeholder={accountNotification} readOnly />
                </div>

                <div>
                    <label><b>Meta Financeira</b></label>
                    <textarea type="text" placeholder={financialGoalNotification} readOnly />
                </div>

                <div>
                    <label><b>Meta Mensal de Investimento</b></label>
                    <textarea type="text" placeholder={investmentGoalNotification} readOnly />
                </div>
            </form>
        </div>
    )
}

export default BodyNotifications;