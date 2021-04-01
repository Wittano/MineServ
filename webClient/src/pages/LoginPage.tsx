import { auth } from "../utils/Auth";
import { redirect } from "../utils/Redirect";
import Form from "../component/forms/Form";
import JwtResponse from "../interfaces/reponse/JwtResponse";

export const LoginPage = () => (
  <Form<JwtResponse>
    title="Login"
    successFunc={(response) => {
      auth(response.data!!.token);
      redirect();
    }}
    action="/auth"
  />
);
