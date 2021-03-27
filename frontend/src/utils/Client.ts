import axios from "axios";
import Cookies from "js-cookie";
import { redirect } from "./Redirect";

const URL: string = "http://localhost:8080/api";

/**
 * Default http client for communtaion with my API
 */
export const client = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * Default http client for communcation with part of API, which is autorizated
 */
export const authClient = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${Cookies.get("jwt_token")}`,
  },
});

/**
 * Check if, JWT is exist. If he isn't exist, user will be redirected to login page
 */
export const refresh = () => {
  if (Cookies.get("jwt_token")) {
    return;
  } else {
    redirect("/login");
  }
};
