import { Button } from "../component/Buttons";
import { useEffect, useState } from "react";
import { CreateForm } from "../component/forms/CreateForm";
import { authClient, refresh } from "../utils/Client";
import { BaseLink } from "../component/Link";
import { Server as ServerComponent } from "../component/Server";
import Server from "../interfaces/Server";

export const AdminPage = () => {
  const [isForm, setIsForm] = useState(false);
  const [server, setServers] = useState(Array<Server>());
  const [wait, setWait] = useState(false);

  useEffect(() => {
    const getServers = async () => {
      setServers(
        await authClient
          .get("/server")
          .then((res) => res.data)
          .catch(refresh)
      );
    };

    getServers();
  }, []);

  const changeForm = () => setIsForm(!isForm);

  let rigthCorner = () => {
    if (!isForm && !wait) {
      return <Button disable={false} text="Create Server" click={changeForm} />;
    } else if (isForm && !wait) {
      return (
        <CreateForm
          cancelClick={changeForm}
          updateServer={setServers}
          servers={server}
          setWait={setWait}
        />
      );
    } else {
      return (
        <p className="text-red-500">Please wait. Server is being created</p>
      );
    }
  };

  const serverList: () => JSX.Element | undefined = () => {
    if (server !== undefined && server.length > 0) {
      return (
        <div className="bg-white shadow-md m-10 p-2">
          {server.map((e) => (
            <ul key={e.id} className="border-b-1 border-solid border-gray-300">
              <ServerComponent
                current={e}
                updateServer={setServers}
                servers={server}
              />
            </ul>
          ))}
        </div>
      );
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center">
        <BaseLink to="/" text="Home" />
        {rigthCorner()}
      </div>
      {serverList()}
    </div>
  );
};
