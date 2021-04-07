import { Difficulty } from "../interfaces/enums/server/Difficulty";
import { Gamemode } from "../interfaces/enums/server/Gamemode";
import { LevelType } from "../interfaces/enums/server/LevelType";
import PropertiesProps from "../interfaces/props/component/PropertiesProps";

const textInputStyle =
  "rounded-sm px-4 py-3 mt-3 focus:outline-none bg-gray-100";
type Enums = Difficulty | Gamemode | LevelType;

function GeneralProperties<T>(
  props: PropertiesProps<T>,
  form: JSX.Element
): JSX.Element {
  return (
    <div className="flex space-x-5 items-baseline place-items-center justify-between">
      <p>{props.name}</p>
      {form}
    </div>
  );
}

export const BooleanProperties = (props: PropertiesProps<boolean>) => {
  const checkbox = () => {
    return (
      <div>
        <input
          className=""
          type="checkbox"
          defaultChecked={props.property}
          onChange={(e) => props.update(e.target.checked.toString())}
        />
      </div>
    );
  };

  return GeneralProperties(props, checkbox());
};

export const NumberProperties = (
  props: PropertiesProps<number>
): JSX.Element => {
  const numberForm = () => {
    return (
      <input
        className={textInputStyle}
        type="text"
        defaultValue={props.property}
        onChange={(e) => props.update(e.target.value)}
      />
    );
  };

  return GeneralProperties(props, numberForm()!);
};

export const StringProperties = (
  props: PropertiesProps<String>
): JSX.Element => {
  const textForm = () => {
    return (
      <input
        className={textInputStyle}
        type="text"
        defaultValue={props.property as string}
        onChange={(e) => props.update(e.target.value)}
      ></input>
    );
  };

  return GeneralProperties(props, textForm());
};

export const EnumProperties = (props: PropertiesProps<Enums>) => {
  const choiceBox = () => {
    const select: (array: string[]) => JSX.Element = (array: string[]) => {
      return (
        <div>
          <select
            defaultValue={props.property}
            onChange={(e) => props.update(e.target.value)}
          >
            {array.map((e) => {
              return <option>{e.toLowerCase()}</option>;
            })}
          </select>
        </div>
      );
    };

    return [Difficulty, Gamemode, LevelType]
      .map((e) => Object.keys(e))
      .filter((e) => e.includes(props.property.toUpperCase()))
      .map((e) => select(e))[0];
  };

  return GeneralProperties<Enums>(props, choiceBox());
};
