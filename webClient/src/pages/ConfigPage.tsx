import React, { useEffect, useState } from "react";
import { BaseLink } from "../component/Link";
import ConfigPageProps from "../interfaces/props/pages/ConfigPageProps";
import { authClient, refresh } from "../utils/Client";
import {
  StringProperties,
  BooleanProperties,
  NumberProperties,
  EnumProperties,
} from "../component/Properties";
import { Difficulty } from "../interfaces/enums/server/Difficulty";
import { Gamemode } from "../interfaces/enums/server/Gamemode";
import { isServerEnum } from "../utils/isEnum";
import { LevelType } from "../interfaces/enums/server/LevelType";
import { DoneButton } from "../component/Buttons";

export const ConfigPage = (props: ConfigPageProps) => {
  const { id } = props.match.params;
  const [properties, setProperties] = useState<any>();
  const [response, setResponse] = useState<string>();

  useEffect(() => {
    const properties = async () => {
      await authClient
        .get(`/properties/${id}`)
        .then((res) => {
          setProperties(res.data.data);
        })
        .catch(refresh);
    };

    properties();
  }, [id]);

  const settings: () => JSX.Element[] | undefined = () => {
    if (properties === undefined) {
      return undefined;
    }

    return Object.keys(properties!).map((e) => {
      const value: string = Reflect.get(properties!, e);
      const updateProperties = (value: string) => {
        let settings = properties!;

        Reflect.set(settings!, e, value);

        setProperties(settings);
      };

      if (RegExp("^[0-9]+$").test(value)) {
        return (
          <NumberProperties
            property={parseInt(value)}
            name={e}
            update={updateProperties}
          />
        );
      } else if (value === "true" || value === "false") {
        return (
          <BooleanProperties
            property={value === "true"}
            name={e}
            update={updateProperties}
          />
        );
      } else if (isServerEnum(value)) {
        return (
          <EnumProperties
            property={value as Difficulty | Gamemode | LevelType}
            name={e}
            update={updateProperties}
          />
        );
      } else {
        return (
          <StringProperties
            property={value as string}
            name={e}
            update={updateProperties}
          />
        );
      }
    });
  };

  const updateClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    authClient
      .put(`/properties/${id}`, properties)
      .then((res) => {
        setResponse("Settings updated!");
        setTimeout(() => {
          setResponse("");
        }, 2000);
      })
      .catch(refresh);
  };

  return (
    <div>
      <BaseLink text="Admin Page" to="/admin" />
      <div className="grid grid-cols-4 gap-4 m-5 2xl:grid-cols-4 md:grid-cols-2 sm:grid-cols-1">
        {settings()}
      </div>
      <div className="flex justify-center space-y-5">
        <DoneButton
          text="Update settings"
          disable={false}
          click={updateClick}
        />
      </div>
      <div className="flex justify-center space-y-5">
        <p className="text-green-700">{response}</p>
      </div>
    </div>
  );
};
