import React from "react";

export default interface CreateFormProps {
  setWait: React.Dispatch<React.SetStateAction<boolean>>;
  cancelClick: () => void;
  servers: any[];
  updateServer: React.Dispatch<React.SetStateAction<any[]>>;
}
