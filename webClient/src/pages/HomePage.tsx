import { isAuth } from "../utils/Auth";
import Cookies from "js-cookie";
import { BaseLink } from "../component/Link";
import { Button } from "../component/Buttons";
import { tokenName } from "../utils/Token";

const logout: () => void = () => {
  Cookies.remove(tokenName);
  window.location.reload();
};

export const HomePage: () => JSX.Element = () => {
  const adminLink = () => {
    if (isAuth()) {
      return (
        <div className="flex ml-16 space-x-8">
          <BaseLink to="/admin" text="Admin page" />
          <Button disable={false} click={logout} text="Logout" />
        </div>
      );
    }
  };

  return (
    <div className="h-full flex justify-center flex-wrap content-center">
      <div className="font-large text-xl space-y-10">
        <h1 className="flow-root text-center text-3xl">Hello World!</h1>
        <div className="w-max flex flex-warp space-x-8">
          <BaseLink to="/register" text="Register page" />
          <BaseLink to="/login" text="Login" />
          {adminLink()}
        </div>
      </div>
    </div>
  );
};
