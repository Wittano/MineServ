import React from "react";

export default interface ButtonsProps {
  disable?: boolean;
  text: string | null;
  click: (e: React.MouseEvent<HTMLButtonElement>) => void;
}
