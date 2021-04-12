import Cookies from "js-cookie";
import { createToken, tokenName } from "./Token";

const JwtRegex = new RegExp(
  "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"
);

/**
 * Check if user is logged in
 * @returns True, if JWT token exist and is valid, otherwise False
 */
export const isAuth: () => boolean = () => {
  const cookie: () => string = () => {
    const it = Cookies.get(tokenName);

    return typeof it === "string" ? it : "";
  };

  return cookie() !== "" && JwtRegex.test(cookie());
};

/**
 * Save JWT token in Cookies in local storage
 * @param token JWT token, which will be got from API
 */
export const auth = (token: string): void => {
  let current = new Date();
  current.setHours(current.getHours() + 1);

  createToken(tokenName, token, current);
};
