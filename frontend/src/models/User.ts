export default class User {
  private readonly username: string;
  private readonly password: string;

  constructor(name: string, passwd: string) {
    this.username = name;
    this.password = passwd;
  }

  /**
   * Check, if user data is correct
   * @returns True, if user login and password is correct, otherwise funtion will return false
   */
  public valid(): boolean {
    const nameRegex = new RegExp("^[\\w.@+-]{5,150}");
    const passwordRegex = new RegExp("^[\\w.@+-]{5,150}");

    return nameRegex.test(this.username) && passwordRegex.test(this.password);
  }
}
