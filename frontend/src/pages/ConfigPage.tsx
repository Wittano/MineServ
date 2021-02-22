export default function ConfigPage(props) {
  const { id } = props.match.params;

  return <p>Works {id}</p>;
}
