import InputProps from "../interfaces/props/component/InputProps";

export default function Input(props: InputProps) {
  return (
    <div className="my-5 text-sm">
      <label htmlFor={props.name} className="block text-block">
        {props.name}:{" "}
      </label>
      <input
        id={props.name}
        className="rounded-sm px-4 py-3 mt-3 focus:outline-none bg-gray-100 w-full"
        onChange={props.onChange}
        type={props.type}
        placeholder={props.name}
      />
    </div>
  );
}
