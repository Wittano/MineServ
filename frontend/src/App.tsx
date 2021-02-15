import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom'
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import AdminPage from "./pages/AdminPage";
import {isAuth} from "./utils/Auth";
import RegisterPage from "./pages/RegisterPage";
import './index.css'

export default function App() {
    const authRoute = () => {
        if (isAuth()) {
            return (
                <Route path='/admin' component={AdminPage}/>
            )
        }
    }

    return (
        <BrowserRouter>
            <Switch>
                <Route exact path='/' component={HomePage}/>
                <Route path='/login' component={LoginPage}/>
                <Route path='/register' component={RegisterPage}/>
                {authRoute()}
                <Route path='*' render={() => {
                    return (
                        <p>Not Found</p>
                    )
                }}/>
            </Switch>
        </BrowserRouter>
    );
}

