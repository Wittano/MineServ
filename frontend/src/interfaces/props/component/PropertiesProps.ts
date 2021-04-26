export default interface PropertiesProps<T> {
  property: T;
  name: string;
  update: (value: string) => void;
}
