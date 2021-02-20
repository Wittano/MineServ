import Button from "../component/Buttons";
import { useEffect, useState } from "react";
import CreateForm from "../component/forms/CreateForm";
import { authClient } from "../utils/Client";
import Server from "../component/Server";

export default function AdminPage() {
  const [isForm, setIsForm] = useState(false);
  const [server, setServers] = useState([]);
  useEffect(() => {
    getServers();
  }, []);

  const getServers = async () => {
    const res = await authClient.get("/server").then((res) => res.data);
    setServers(res);
  };
  const changeForm = () => {
    setIsForm(!isForm);
  };

  return (
    <div>
      {!isForm ? (
        <Button text="Create Server" click={changeForm} />
      ) : (
        <CreateForm cancelClick={changeForm} />
      )}
      <div>
        <ul>
          {server.map((e) => (
            <Server name={e.name} key={e.id} status={e.status} />
          ))}
        </ul>
      </div>
    </div>
  );
}
