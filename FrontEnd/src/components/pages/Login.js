import BodyLogin from "../layout/body_login/BodyLogin";
import HeaderLogin from "../layout/header_login/HeaderLogin";
import SidebarLogin from "../layout/sidebar_login/SidebarLogin";

function Login() {
  return (
    <div>
      <SidebarLogin />
      <HeaderLogin />
      <BodyLogin />
    </div>
  )
}

export default Login;