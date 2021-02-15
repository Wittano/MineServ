import {Link} from 'react-router-dom'
import {useState} from "react";
import User from "../models/User";
import {auth} from "../utils/Auth";
import {client} from "../utils/Client";
import redirect from "../utils/Redirect";

const errorMessage = () => {
    return (
        <div>
            <span>Incorrect login or password</span>
        </div>
    )
}

export default function LoginPage() {
    // Hooks
    const [name, setName] = useState("")
    const [passwd, setPassword] = useState("")
    const [error, setError] = useState(false)
    const nameInput = e => setName(e.target.value.trim())
    const passwordInput = e => setPassword(e.target.value.trim())

    const login = async () => {
        const user = new User(name, passwd)

        if (!user.valid()) {
            setError(true)
            return
        }

        const res: any | null = await client.post(
            '/token',
            user
        ).then(res => {
            setError(false)

            return res.data
        }).catch(err => {
            console.error(err.message)
            setError(true)

            return null
        })

        if (res) {
            auth(res)
        }

        redirect()
    };

    let err: JSX.Element | null = error ? errorMessage() : null;

    return (
        <div>
            <header>
                <Link to='/'>Home page</Link>
                <p>Login page</p>
            </header>
            <div>
                <div>
                    <label htmlFor='username'>Username: </label>
                    <input id='username' onChange={nameInput} type='text' placeholder='username'/>
                </div>
                <div>
                    <label htmlFor='password'>Password: </label>
                    <input id='password' onChange={passwordInput} type='password' placeholder='password'/>
                </div>
                {err}
                <button onClick={login}>Login</button>
            </div>
        </div>
    )
}