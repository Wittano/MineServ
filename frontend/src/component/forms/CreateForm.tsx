import { Input } from "../Input";
import React, { useEffect, useState } from "react";
import { DoneButton, RefuseButton } from "../Buttons";
import { authClient, refresh } from "../../utils/Client";
import Error from "../Error";
import CreateFormProps from "../../interfaces/props/component/CreateFormProps";
import Version from "../../models/Version";
import APIResponse from "../../interfaces/reponse/APIResponse";

const getVersion = async () => {
  const response: APIResponse<Array<Version>> = await authClient
    .get("/server/version")
    .then((res) => res.data);

  return response.data;
};

export const CreateForm = (props: CreateFormProps) => {
  // Hooks
  const [serverName, setServerName] = useState("");
  const [version, setVersion] = useState(Array<Version>());
  const [select, setSelecte] = useState(version[0]);
  const [error, setError] = useState("");
  useEffect(() => {
    const versions = async () => {
      setVersion((await getVersion()) as Array<Version>);
    };

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

  const nameInput = (event: React.ChangeEvent<HTMLInputElement>) =>
    setServerName(event.target.value.trim());

  const selectInput = (event: React.ChangeEvent<HTMLSelectElement>) =>
    setSelecte(version.find((e: Version) => e.name === event.target.value)!!);

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
            {version.map((element: Version) => (
              <option>{element.name}</option>
            ))}
          </select>
          {showError()}
        </div>
        <div className={divClass}>
          <RefuseButton
            disable={false}
            text="Cancel"
            click={props.cancelClick}
          />
          <DoneButton
            disable={serverName === ""}
            text="Create"
            click={createServer}
          />
        </div>
      </div>
    </div>
  );
};
