import Input from "../Input";
import { useEffect, useState } from "react";
import { DoneButton, RefuseButton } from "../Buttons";
import { authClient } from "../../utils/Client";

const getVersion = async () => {
  const response: Array<any> = await authClient
    .get("/server/version")
    .then((res) => res.data);

  return response.map((e) => e.version);
};

export default function CreateForm(props) {
  const versions = async () => {
    setVersion(await getVersion());
  };

  // Hooks
  const [serverName, setServerName] = useState("");
  const [version, setVersion] = useState([]);
  const [select, setSelecte] = useState("");
  const [error, setError] = useState("");
  const [wait, setWait] = useState(false);
  useEffect(() => {
    versions();
  }, []);

  const createServer = async (e) => {
    setWait(true);

    const res = await authClient
      .post("/server/create", {
        name: serverName,
        version: select,
      })
      .catch((err) => err);

    setWait(false);

    if (res.status === 200) {
      setError("");
      props.cancelClick();
    } else {
      setError(res.message);
    }
  };

  const waitNotify = () => {
    if (wait) {
      return (
        <p className="text-red-500">Please wait. Server is being created</p>
      );
    }
  };

  const showError = () => {
    if (error !== "") {
      return <p>{error}</p>;
    }
  };

  const nameInput = (e) => setServerName(e.target.value.trim());
  const selectInput = (e) => setSelecte(e.target.value);

  const divClass = "flex space-x-4 items-center mr-10 ml-10";
  return (
    <div>
      <h2 className="text-xl text-center">Create server</h2>
      <div className="flex justify-between items-center">
        <div className={divClass + " flex-grow"}>
          <Input name="Name" type="text" onChange={nameInput} />
          <select
            onLoad={selectInput}
            onChange={selectInput}
            className="border bg-white rounded px-3 py-2 outline-none"
          >
            {version.map((element) => {
              return <option>{element}</option>;
            })}
          </select>
          {waitNotify}
          {showError}
        </div>
        <div className={divClass}>
          <RefuseButton text="Cancel" click={props.cancelClick} />
          <DoneButton
            disable={serverName === ""}
            text="Create"
            click={createServer}
          />
        </div>
      </div>
    </div>
  );
}
