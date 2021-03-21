import axios from "axios";
import Cookies from "js-cookie";
import redirect from "./Redirect";
import { createToken } from "./Token";

const URL: string = "http://localhost:8080/api";

export const client = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const authClient = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${Cookies.get("jwt_token")}`,
  },
});

export async function refresh() {
  if (Cookies.get("jwt_token")) {
    return;
  }

  const token = await client
    .post("/token/refresh", {
      token: Cookies.get("jwt_refresh"),
    })
    .then((res) => res.data.access)
    .catch((_) => redirect("/login"));

  let now: Date = new Date();
  now.setHours(now.getHours() + 1);

  createToken("jwt_token", token, now);
}
