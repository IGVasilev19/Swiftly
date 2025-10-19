export enum AccountProviders {
  Credentials = "Credentials",
}

export type User = {
  _id: string;
  username: string;
  email: string;
  provider: AccountProviders;
  hasPassword: boolean;
  createdAt: string;
  updatedAt: string;
};
