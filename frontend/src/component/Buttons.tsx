function BaseButton(props, className: string): JSX.Element {
  return (
    <div>
      <button
        className={className}
        disabled={props.disable}
        onClick={props.click}
      >
        {props.text}
      </button>
    </div>
  );
}

export function DoneButton(props) {
  return BaseButton(
    props,
    "inline-block px-6 py-2 text-xs" +
      " font-medium leading-6 text-center text-white" +
      " uppercase transition bg-green-500 disabled:opacity-50" +
      " disabled:cursor-not-allowed disabled:pointer-events-none rounded shadow" +
      " ripple hover:shadow-lg hover:bg-green-600 focus:outline-none"
  );
}

export function RefuseButton(props) {
  return BaseButton(
    props,
    "inline-block px-6 py-2 text-xs" +
      " font-medium leading-6 text-center text-white" +
      " uppercase transition bg-red-500 rounded shadow " +
      "ripple hover:shadow-lg hover:bg-red-600 focus:outline-none"
  );
}

export default function Button(props) {
  return BaseButton(
    props,
    "inline-block px-6 py-2 text-xs font-medium leading-6 text-center text-white" +
      " uppercase transition bg-yellow-500 rounded shadow ripple" +
      " hover:shadow-lg hover:bg-yellow-600 focus:outline-none"
  );
}
