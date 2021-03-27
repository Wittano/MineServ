import { redirect } from "../utils/Redirect";
import Form from "../component/forms/Form";

export const RegisterPage = () => (
  <Form<any>
    title="Register"
    action="/user"
    successFunc={() => redirect("/login")}
  />
);
