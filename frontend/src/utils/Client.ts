import axios from "axios";
import Cookies from "js-cookie";

export const client = axios.create({
    baseURL: 'http://localhost:8000/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

export const authClient = axios.create({
    baseURL: 'http://localhost:8000/api',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Cookies.get('jwt_token')}`
    }
})