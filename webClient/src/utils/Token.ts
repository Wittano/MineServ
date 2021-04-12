import * as Cookies from "js-cookie";

export const tokenName = "token";

/**
 * Create JWT token as Cookie
 * @param name cookie name
 * @param data right JWT token
 * @param time time, which JWT cookie will be expired
 */
export const createToken = (name: string, data: string, time: Date) => {
  // Cookies.set(name, data, {
  //   secure: true,
  //   expires: time,
  //   domain: window.location.origin
  // });
  document.cookie = `${name}=${data};expires=${time}`;
};

/**
 * Get JWT token by cookie name
 * @param name JWT cookie name
 * @returns JWT token as string, or undefined, when cookie wasn't found
 */
export const getToken = (name: string = tokenName): string | undefined =>
  Cookies.get(name);
