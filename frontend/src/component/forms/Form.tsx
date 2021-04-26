import React, { useState } from "react";
import FormProps from "../../interfaces/props/component/FormProps";
import User from "../../models/User";
import { client, refresh } from "../../utils/Client";
import Error from "../Error";
import Input from "../Input";

export default function Form<T>(props: FormProps<T>) {
  // Hooks
  const [name, setName] = useState("");
  const [passwd, setPassword] = useState("");
  const [error, setError] = useState(false);
  const [msg, setMsg] = useState("");

  // Update functions
  const nameInput = (e: React.ChangeEvent<HTMLInputElement>) => setName(e.target.value.trim());
  const passwordInput = (e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value.trim());

  const action = async () => {
    const user = new User(name, passwd);

    if (!user.valid()) {
      setError(true);
      setMsg("Illegal login or password");
      return;
    }

    const res: any | null = await client
      .post(props.action, user)
      .then((res) => {
        setError(false);

        return res.data;
      })
      .catch(async (_) => {
        await refresh();
        setError(true);
        setMsg("Incorrect username or password");

        return null;
      });

    if (res) {
      props.successFunc(res);
    }
  };

  const err: JSX.Element | null = error ? <Error msg={msg} /> : null;

  return (
    <div className="bg-white lg:w-4/12 md:6/12 w-10/12 m-auto my-10 shadow-md">
      <div className="py-8 px-8 rounded-xl">
        <h1 className="font-medium text-2xl mt-3 text-center">
          {props.title} page
        </h1>
        <div>
          <Input name="Username" type="text" onChange={nameInput} />
          <Input name="Password" type="password" onChange={passwordInput} />
          {err}
          <button
            className="block text-center text-white bg-yellow-500 p-3 duration-300 rounded-sm hover:bg-yellow-600 w-full"
            onClick={action}
          >
            {props.title}
          </button>
        </div>
      </div>
    </div>
  );
}
