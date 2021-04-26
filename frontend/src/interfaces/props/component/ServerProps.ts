import React from "react";
import Server from "../../Server";

export default interface ServerProps {
  current: Server;
  servers: Array<Server>;
  updateServer: React.Dispatch<React.SetStateAction<Server[]>>;
}
