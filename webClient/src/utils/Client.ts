import axios from "axios";
import Cookies from "js-cookie";
import { redirect } from "./Redirect";
import { tokenName } from "./Token";

const { REACT_APP_API_URL } = process.env;
const URL: string = REACT_APP_API_URL || "http://localhost:8080/api";

/**
 * Default http client for communication with my API
 */
export const client = axios.create({
  baseURL: URL,
});

/**
 * Default http client for communication with part of API, which is authorized
 */
export const authClient = axios.create({
  baseURL: URL,
  headers: {
    Authorization: `Bearer ${Cookies.get(tokenName)}`,
  },
});

/**
 * Check if, JWT is exist. If he isn't exist, user will be redirected to login page
 */
export const refresh = () => {
  if (Cookies.get(tokenName)) {
    return;
  } else {
    redirect("/login");
  }
};
