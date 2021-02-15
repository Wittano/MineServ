import * as Cookies from "js-cookie";

export const createToken = (name: string, data: string, time: Date) => {
    Cookies.set(name, data, {
        secure: true,
        expires: time
    })
}

export const getToken = (name: string): string | undefined => {
    return Cookies.get(name)
}

