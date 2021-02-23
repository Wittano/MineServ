import Button from "../component/Buttons";
import { useEffect, useState } from "react";
import CreateForm from "../component/forms/CreateForm";
import { authClient, refresh } from "../utils/Client";
import Server from "../component/Server";
import BaseLink from "../component/Link";

export default function AdminPage() {
  const [isForm, setIsForm] = useState(false);
  const [server, setServers] = useState([]);
  const [wait, setWait] = useState(false);
  useEffect(() => {
    getServers();
  }, []);

  const getServers = async () => {
    const res = await authClient
      .get("/server")
      .then((res) => res.data)
      .catch(async () => {
        await refresh();
      });
    setServers(res);
  };

  const changeForm = () => {
    setIsForm(!isForm);
  };

  let rigthCorner = () => {
    if (!isForm && !wait) {
      return <Button text="Create Server" click={changeForm} />;
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

  return (
    <div>
      <div className="flex justify-between items-center">
        <BaseLink to="/" text="Home" />
        {rigthCorner()}
      </div>
      <div className="bg-white shadow-md m-10 p-2">
        {server.map((e) => {
          return (
            <ul className="border-b-1 border-solid border-gray-300">
              <Server
                key={e.id}
                data={e}
                updateServer={setServers}
                servers={server}
              />
            </ul>
          );
        })}
      </div>
    </div>
  );
}
