import redirect from "../utils/Redirect";
import Form from "../component/Form";

export default function RegisterPage() {
    const success = () => {
        redirect('/login')
    }

    return (
        <Form title='Register' action='/user' successFunc={success}/>
    )
}