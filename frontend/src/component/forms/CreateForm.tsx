import Input from "../Input";
import React, { useEffect, useState } from "react";
import { DoneButton, RefuseButton } from "../Buttons";
import { authClient, refresh } from "../../utils/Client";
import Error from "../Error";
import CreateFormProps from "../../interfaces/props/component/CreateFormProps";
import Version from "../../interfaces/Version";

const getVersion = async () => {
  const response: Array<Version> = await authClient
    .get("/server/version")
    .then((res) => res.data);

  return response.map((e) => e.version);
};

export default function CreateForm(props: CreateFormProps) {
  const versions = async () => {
    setVersion(await getVersion());
  };

  // Hooks
  const [serverName, setServerName] = useState("");
  const [version, setVersion] = useState<string[]>([]);
  const [select, setSelecte] = useState(version[0]);
  const [error, setError] = useState("");
  useEffect(() => {
    versions();
  }, []);

  const createServer = async () => {
    props.setWait(true);

    const res = await authClient
      .post("/server/create", {
        name: serverName,
        version: !select ? version[0] : select,
      })
      .catch(async (err) => {
        await refresh();
        return err;
      });

    props.setWait(false);

    if (res.status === 200) {
      setError("");
      props.cancelClick();
    } else {
      setError(res.message);
    }

    props.updateServer([...props.servers, res.data]);
  };

  const showError = () => {
    if (error !== "") {
      return <Error msg={error} />;
    }
  };

  const nameInput = (e: React.ChangeEvent<HTMLInputElement>) => setServerName(e.target.value.trim());
  const selectInput = (e: React.ChangeEvent<HTMLSelectElement>) => setSelecte(e.target.value);

  const divClass = "flex space-x-4 items-center mr-10 ml-10";
  return (
    <div>
      <div className="flex justify-between items-center">
        <div className={divClass + " flex-grow"}>
          <Input name="Name" type="text" onChange={nameInput} />
          <select
            onChange={selectInput}
            className="border bg-white rounded px-3 py-2 outline-none"
          >
            {version.map((element) => {
              return <option>{element}</option>;
            })}
          </select>
          {showError()}
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
