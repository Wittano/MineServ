import { Difficulty } from "../interfaces/enums/server/Difficulty";
import { Gamemode } from "../interfaces/enums/server/Gamemode";
import { LevelType } from "../interfaces/enums/server/LevelType";

export const isServerEnum: (value: string) => boolean = (value) =>
  [Gamemode, Difficulty, LevelType]
    .map((e) => Object.keys(e))
    .flatMap((e) => [...e])
    .includes(value.toUpperCase());
