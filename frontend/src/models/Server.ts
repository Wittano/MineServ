export default interface Server {
  id: number;
  name: string;
  version: {
    id: number;
    version: string;
    link: string;
  };
  owner: {
    id: number;
    name: string;
  };
  status: number;
}
