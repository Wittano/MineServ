import {useState} from "react";
import {client} from "../utils/Client";
import User from "../models/User";
import redirect from "../utils/Redirect";

export default function RegisterPage() {
    const [name, setName] = useState('')
    const [passwd, setPasswd] = useState('')
    const [err, setErr] = useState(false)

    const nameInput = e => setName(e.target.value.trim())
    const passwdInput = e => setPasswd(e.target.value.trim())


    const register = async () => {
        const user = new User(name, passwd)

        if(!user.valid()){
            setErr(true)
            return
        }

        const isRegistered: boolean = await client.post(
            '/user',
            user
        ).then(res => {
            setErr(false)
            return res.status == 201
        }).catch(err => {
            console.error(err)
            return false
        })

        if(isRegistered){
            redirect()
        }
    }

    const error = err ? (
        <div>
            <span>Incorrect username or password</span>
        </div>
    ) : null

    return (
        <div>
            <p>Register</p>
            <div>
                <div>
                    <label htmlFor='user'>Username: </label>
                    <input id='user' onChange={nameInput} type='text' placeholder='username'/>
                </div>
                <div>
                    <label htmlFor='passwd'>Password: </label>
                    <input id='passwd' onChange={passwdInput} type='text' placeholder='password'/>
                </div>
                {error}
                <button onClick={register}>Register</button>
            </div>
        </div>
    )
}