import BodyRegister from "../layout/body_register/BodyRegister";
import HeaderLogin from "../layout/header_login/HeaderLogin";
import SidebarLogin from "../layout/sidebar_login/SidebarLogin";

function Register() {
  return (
    <div>
      <SidebarLogin />
      <HeaderLogin />
      <BodyRegister />
    </div>
  )
}

export default Register;