import ConfigPageProps from "../interfaces/props/pages/ConfigPageProps";

export const ConfigPage = (props: ConfigPageProps) => {
  const id = props.match.params;

  return <p>Works {id}</p>;
};
