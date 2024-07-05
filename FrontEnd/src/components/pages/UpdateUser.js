import BodyUpdateUser from "../layout/body_updateuser/BodyUpdateUser";
import HeaderHome from "../layout/header_home/HeaderHome";
import SidebarHome from "../layout/sidebar_home/SidebarHome";
import ErrorPage from "./ErrorPage";

function Register() {
  const token = localStorage.getItem('token');

  return (
      <div>
          {token.length > 20 &&
              <>
                  <SidebarHome />
                  <BodyUpdateUser />
                  <HeaderHome />
              </>
          }
          {token.length < 20 && 
              <ErrorPage />
          }
      </div>
  )
}

export default Register;