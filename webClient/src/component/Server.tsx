import { useState } from "react";
import { ServerStatus } from "../interfaces/enums/ServerStatus";
import ServerProps from "../interfaces/props/component/ServerProps";
import { authClient, refresh } from "../utils/Client";
import { DoneButton, RefuseButton } from "./Buttons";
import { BaseLink } from "./Link";

export const Server = (props: ServerProps) => {
  const [status, setStatus] = useState(props.current.status);

  const info = (text: string, value: string) => {
    return (
      <div>
        <p className="text-xs text-gray-400">{text}</p>
        <p>{value}</p>
      </div>
    );
  };

  const del = async () => {
    await authClient.delete(`/server/${props.current.id}`).catch(refresh);

    props.updateServer(props.servers.filter((e) => e.id !== props.current.id));
  };

  const start = async () => {
    await authClient.put(`/server/start/${props.current.id}`).catch(refresh);
    setStatus(ServerStatus.RUNNING);
  };

  const stop = async () => {
    await authClient.put(`/server/stop/${props.current.id}`).catch(refresh);
    setStatus(ServerStatus.STOP);
  };

  return (
    <div className="flex justify-between m-10 align-baseline">
      <div className="flex space-x-7">
        {info("Server name", props.current.name)}
        {info("Version", props.current.version.version)}
        {info("Status", props.current.status.toString().toLocaleLowerCase())}
      </div>
      <div className="flex space-x-4">
        <BaseLink to={"/server/" + props.current.id} text="Edit" />
        <RefuseButton disable={false} text="Delete" click={del} />
        {status === ServerStatus.STOP ? (
          <DoneButton
            text="Start"
            click={start}
            disable={status !== ServerStatus.STOP}
          />
        ) : (
          <RefuseButton
            text="Stop"
            click={stop}
            disable={status !== ServerStatus.RUNNING}
          />
        )}
      </div>
    </div>
  );
};
