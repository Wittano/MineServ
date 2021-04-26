import { useEffect, useState } from "react";
import ServerProps from "../interfaces/props/component/ServerProps";
import { authClient, refresh } from "../utils/Client";
import { DoneButton, RefuseButton } from "./Buttons";
import BaseLink from "./Link";

// Return descriptive name of status
const nameStatus = (status: number) => {
  let x: string = "";

  switch (status) {
    case 1:
      x = "Stopped";
      break;
    case 2:
      x = "Starting";
      break;
    case 3:
      x = "Running";
      break;
  }

  return x;
};

export const Server = (props: ServerProps) => {
  const [version, setVersion] = useState("");
  const [status, setStatus] = useState(props.current.status);

  useEffect(() => {
    const getVersion = async () => {
      setVersion(
        await authClient
          .get(`/server/version/${props.current.version}`)
          .then((res) => res.data[0].version)
          .catch(async () => {
            await refresh();
          })
      );
    };

    getVersion();
  }, [props.current.version]);

  const info = (text: string, value: string) => {
    return (
      <div>
        <p className="text-xs text-gray-400">{text}</p>
        <p>{value}</p>
      </div>
    );
  };

  const del = async () => {
    await authClient
      .delete(`/server/delete/${props.current.id}`)
      .catch(async () => {
        await refresh();
      });

    props.updateServer(props.servers.filter((e) => e.id !== props.current.id));
  };

  const start = async () => {
    setStatus(2);
    await authClient
      .post(`/server/start/${props.current.id}`)
      .catch(async (_) => {
        await refresh();
      });
    setStatus(3);
  };

  const stop = async () => {
    await authClient
      .post(`/server/stop/${props.current.id}`)
      .catch(async (_) => {
        await refresh();
      });
    setStatus(1);
  };

  return (
    <div className="flex justify-between m-10 align-baseline">
      <div className="flex space-x-7">
        {info("Server name", props.current.name)}
        {info("Version", version)}
        {info("Status", nameStatus(status))}
      </div>
      <div className="flex space-x-4">
        <BaseLink to={"/server/" + props.current.id} text="Edit" />
        <RefuseButton text="Delete" click={del} />
        {status === 1 ? (
          <DoneButton text="Start" click={start} disable={status !== 1} />
        ) : (
          <RefuseButton text="Stop" click={stop} disable={status !== 3} />
        )}
      </div>
    </div>
  );
}
