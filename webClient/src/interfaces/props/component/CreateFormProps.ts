import React from "react";
import Server from "../../Server";

export default interface CreateFormProps {
  setWait: React.Dispatch<React.SetStateAction<boolean>>;
  cancelClick: () => void;
  servers: Server[];
  updateServer: React.Dispatch<React.SetStateAction<any[]>>;
}
