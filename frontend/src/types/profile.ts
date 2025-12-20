export interface Profile {
  id?: number;
  fullName: string;
  phone: string;
  avatarUrl?: string;
  [key: string]: unknown;
}
