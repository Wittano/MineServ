import { auth } from "../utils/Auth";
import redirect from "../utils/Redirect";
import Form from "../component/forms/Form";

export default function LoginPage() {
  const success = (res: string[]) => {
    auth(res);
    redirect();
  };

  return <Form<string[]> title="Login" successFunc={success} action="/token" />;
}
