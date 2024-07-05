import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Login from "./components/pages/Login";
import Register from "./components/pages/Register";
import Home from "./components/pages/Home";
import Account from "./components/pages/Account";
import Notifications from "./components/pages/Notifications";
import Minimalism from "./components/pages/Minimalism";
import Goals from "./components/pages/Goals";
import Transactions from "./components/pages/Transactions";
import Help from "./components/pages/Help";
import UpdateUser from "./components/pages/UpdateUser";


function App() {

  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Login />}></Route>
        <Route path="/register" element={<Register />}></Route>
        
        <Route path="/home" element={<Home />}></Route>
        <Route path="/notifications" element={<Notifications />}></Route>
        <Route path="/account" element={<Account />}></Route>
        <Route path="/transactions" element={<Transactions />}></Route>
        <Route path="/goals" element={<Goals />}></Route>
        <Route path="/minimalism" element={<Minimalism />}></Route>
        <Route path="/help" element={<Help />}></Route>
        <Route path="/profile" element={<UpdateUser />}></Route>
      </Routes>
    </Router>
  )
}

export default App;
