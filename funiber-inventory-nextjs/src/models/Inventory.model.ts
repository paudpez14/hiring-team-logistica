import { User } from "./User.model";

export interface Inventory {
    id: number;
    stockQuantity: number;
    length: number;
    width: number;
    height: number;
    createdAt: Date;
    updatedAt: Date;
    createdBy: User;
    updatedBy: User;
    isActive: string;
  }