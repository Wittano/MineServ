import Cookies from "js-cookie";
import { createToken } from "./Token";

const regex = new RegExp(
  "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"
);

export const isAuth = () => {
  const cookie = Cookies.get("jwt_token");

  return cookie && regex.test(cookie);
};

export const auth = (tokens: any) => {
  let current = new Date();
  current.setHours(current.getHours() + 1);

  createToken("jwt_token", tokens.access, current);

  current.setHours(current.getHours() + 23);
  createToken("jwt_refresh", tokens.refresh, current);
};
