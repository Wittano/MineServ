import APIResponse from "../../reponse/APIResponse";

export default interface FormProps<T> {
  title: string;
  action: string;
  successFunc: (response: T) => void;
}
