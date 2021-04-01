import React from "react";
import Server from "../../../models/Server";

export default interface ServerProps {
  current: Server;
  servers: Array<Server>;
  updateServer: React.Dispatch<React.SetStateAction<Server[]>>;
}
