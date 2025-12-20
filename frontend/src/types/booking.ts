import type { Listing } from "./listing";
import type { Profile } from "./profile";

export interface Booking {
  id?: number;
  listing: Listing;
  renter: Profile;
  startAt: Date;
  endAt: Date;
  createdAt?: Date;
  status: string;
  totalPrice: number;
  [key: string]: unknown;
}
