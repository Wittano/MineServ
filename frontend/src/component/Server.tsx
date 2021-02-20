export default function Server(props) {
  return (
    <div>
      <p key={props.key}>{props.name}</p>
      <p>{props.status}</p>
    </div>
  );
}
