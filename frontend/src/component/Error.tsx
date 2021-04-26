import ErrorProps from "../interfaces/props/component/ErrorProps";

export default function Error(props: ErrorProps) {
  return (
    <div className="text-center p-3">
      <span className="text-red-600">{props.msg}</span>
    </div>
  );
}
