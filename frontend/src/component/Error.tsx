export default function Error(props) {
  return (
    <div className="text-center p-3">
      <span className="text-red-600">{props.msg}</span>
    </div>
  );
}
