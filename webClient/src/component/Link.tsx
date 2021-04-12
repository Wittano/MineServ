import { Link } from "react-router-dom";
import LinkProps from "../interfaces/props/component/LinkProps";

export const BaseLink = (props: LinkProps) => {
  return (
    <Link
      className="inline-block px-6 py-2 text-xs font-medium leading-6 text-center text-white uppercase transition bg-yellow-500 rounded shadow ripple hover:shadow-lg hover:bg-yellow-600 focus:outline-none"
      to={props.to}
    >
      {props.text}
    </Link>
  );
};
