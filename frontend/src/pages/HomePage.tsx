import {Link} from 'react-router-dom'
import {isAuth} from "../utils/Auth";

export default function HomePage() {
    const link = "bg-yellow-500 p-3 rounded hover:bg-yellow-400 text-white"

    const adminLink = () => {
        if (isAuth()) {
            return (
                <Link className={link} to='/admin'>Admin page</Link>
            )
        }
    }

    return (
        <div className='h-full flex justify-center flex-wrap content-center'>
            <div className='font-large text-xl space-y-10'>
                <h1 className='flow-root text-center text-3xl'>Hello World!</h1>
                <div className="flow-root space-x-8">
                    <Link className={link} to='/register'>Register page</Link>
                    <Link className={link} to='/login'>Login page</Link>
                    {adminLink()}
                </div>
            </div>
        </div>
    )
}