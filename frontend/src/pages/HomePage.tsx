import {Link} from 'react-router-dom'
import {isAuth} from "../utils/Auth";

export default function HomePage() {
    const adminLink = () => {
        if (isAuth()) {
            return (
                <Link to='/admin'>Admin page</Link>
            )
        }
    }

    return (
        <div>
            <div>
                <Link to='/register'>Register page</Link>
                <Link to='/login'>Login page</Link>
                {adminLink()}
            </div>
            <p>Hello World!</p>
        </div>
    )
}