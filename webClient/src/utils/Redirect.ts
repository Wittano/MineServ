/**
 * Redirect to specific location on website
 * @param to Path, where user will be redirect
 */
export const redirect = (to: string = "/") => {
  const endpoint = to === "/" ? "" : to;

  window.location.replace(`${window.location.origin}${endpoint}`);
};
