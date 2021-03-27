import Cookies from "js-cookie";
import { createToken } from "./Token";

const JwtRegex = new RegExp(
  "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"
);

/**
 * Check if user is logged in
 * @returns True, if JWT token exist and is vaild, otherwise False
 */
export const isAuth: () => boolean = () => {
  const cookie: () => string = () => {
    const it = Cookies.get("jwt_token");

    return typeof it === "string" ? it : "";
  };

  return cookie() !== "" && JwtRegex.test(cookie());
};

/**
 * Save JWT token in Cookies in local storage
 * @param tokens JWT token, which will be got from API
 */
export const auth = (tokens: string): void => {
  let current = new Date();
  current.setHours(current.getHours() + 1);

  createToken("jwt_token", tokens, current);
};
